package appintegrity.maskipli.id

import appintegrity.maskipli.id.config.ServiceConfig
import io.ktor.server.application.*
import appintegrity.maskipli.id.plugins.*
import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val environment = System.getenv()["ENVIRONMENT"] ?: handleDefaultEnvironment()
    val defaultConfig = getDefaultConfig(environment, HoconApplicationConfig(ConfigFactory.load()))
    embeddedServer(
        factory = Netty,
        port = defaultConfig.port,
        configure = {
            requestReadTimeoutSeconds = 30
            responseWriteTimeoutSeconds = 30
        },
        module = Application::installModule
    ).start(wait = true)
}

private fun handleDefaultEnvironment(): String {
    println("default environment 'dev'")
    return "dev"
}

private fun Application.installModule() {
    configKoin()
    configureRouting()
}

private fun getDefaultConfig(
    environment: String,
    hoconConfig: HoconApplicationConfig
): ServiceConfig {
    val hoconEnvironment = hoconConfig.config("ktor.deployment.$environment")
    return ServiceConfig(
        host = hoconEnvironment.property("host").getString(),
        port = Integer.parseInt(hoconEnvironment.property("port").getString())
    )
}
