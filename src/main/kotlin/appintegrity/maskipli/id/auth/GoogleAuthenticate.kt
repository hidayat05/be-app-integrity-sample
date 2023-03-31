package appintegrity.maskipli.id.auth

import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.playintegrity.v1.PlayIntegrityScopes
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import java.io.InputStream

class GoogleAuthenticate {

    companion object Factory {

        private val scopes = listOf(
            PlayIntegrityScopes.PLAYINTEGRITY
        )

        fun install(inputStream: InputStream): Triple<NetHttpTransport, JacksonFactory, HttpCredentialsAdapter> {
            val jacksonFactory = JacksonFactory.getDefaultInstance()
            val httpTransport = NetHttpTransport()
            val googleCredentials = GoogleCredentials.fromStream(inputStream)
                .createScoped(scopes)
            val httpInitializer = HttpCredentialsAdapter(googleCredentials)
            return Triple(httpTransport, jacksonFactory, httpInitializer)
        }
    }
}
