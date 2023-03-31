package appintegrity.maskipli.id.repository

import appintegrity.maskipli.id.model.response.NonceResponse
import appintegrity.maskipli.id.model.response.ValidateNonceResponse
import com.google.api.services.playintegrity.v1.PlayIntegrity
import com.google.api.services.playintegrity.v1.model.DecodeIntegrityTokenRequest
import com.google.api.services.playintegrity.v1.model.DecodeIntegrityTokenResponse
import io.ktor.util.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface CommonRepository {
    suspend fun getNonce(
        deviceId: String
    ): NonceResponse

    suspend fun validateNonce(
        deviceId: String,
        nonce: String
    ): ValidateNonceResponse

    suspend fun validateToken(
        token: String
    ): DecodeIntegrityTokenResponse
}

class CommonRepositoryImpl : CommonRepository, KoinComponent {

    companion object {
        private const val PACKAGE_NAME = "id.maskipli.com"
    }

    private val playIntegrity by inject<PlayIntegrity>()

    override suspend fun getNonce(
        deviceId: String
    ): NonceResponse {
        val nonce = StatelessHmacNonceManager(deviceId.toByteArray()).newNonce()
        return NonceResponse(nonce = nonce)
    }

    override suspend fun validateNonce(
        deviceId: String,
        nonce: String
    ): ValidateNonceResponse {
        val isValid = StatelessHmacNonceManager(deviceId.toByteArray()).verifyNonce(nonce)
        return ValidateNonceResponse(isValid = isValid)
    }

    override suspend fun validateToken(
        token: String
    ): DecodeIntegrityTokenResponse {
        return playIntegrity.v1()
            .decodeIntegrityToken(
                PACKAGE_NAME,
                DecodeIntegrityTokenRequest().setIntegrityToken(token)
            ).execute()
    }
}
