package appintegrity.maskipli.id.plugins

import appintegrity.maskipli.id.auth.GoogleAuthenticate
import appintegrity.maskipli.id.repository.CommonRepository
import appintegrity.maskipli.id.repository.CommonRepositoryImpl
import com.fasterxml.jackson.databind.SerializationFeature
import com.google.api.services.playintegrity.v1.PlayIntegrity
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.util.*
import io.ktor.util.converters.*
import io.netty.handler.codec.DefaultHeaders
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger
import org.slf4j.event.Level

fun Application.configKoin() {

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
        callIdMdc("call-id")
    }

    install(CallId) {
        header(HttpHeaders.XRequestId)
        verify { callId: String ->
            callId.isNotEmpty()
        }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    val (transport, jacksonFactory, credential) = GoogleAuthenticate.install(
        this.environment.classLoader.getResourceAsStream("credentials.json")
    )

    install(Koin){
        SLF4JLogger()
        modules(
            module {
                single<PlayIntegrity> { PlayIntegrity.Builder(transport, jacksonFactory, credential).build() }
                single<CommonRepository> { CommonRepositoryImpl() }
            }
        )
    }
}
