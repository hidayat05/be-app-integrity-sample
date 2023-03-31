package appintegrity.maskipli.id.plugins

import appintegrity.maskipli.id.model.request.GetNonceRequest
import appintegrity.maskipli.id.model.request.ValidateNonceRequest
import appintegrity.maskipli.id.model.request.ValidateTokenRequest
import appintegrity.maskipli.id.repository.CommonRepository
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val commonRepository by inject<CommonRepository>()

    routing {

        get("/") {
            call.respondText("Sample app integrity")
        }

        post("/get-nonce") {
            val params = call.receive<GetNonceRequest>()
            call.respond(commonRepository.getNonce(params.deviceId))
        }

        post("/validate-nonce") {
            val params = call.receive<ValidateNonceRequest>()
            call.respond(commonRepository.validateNonce(params.deviceId, params.nonce))
        }

        post("/validate-token") {
            val params = call.receive<ValidateTokenRequest>()
            call.respond(commonRepository.validateToken(params.token))
        }
    }
}
