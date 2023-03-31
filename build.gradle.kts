repositories {
    mavenCentral()
}

plugins {
    kotlin("jvm") version libs.versions.kotlin
    id("io.ktor.plugin") version libs.versions.ktor.server
    id("org.jetbrains.kotlin.plugin.serialization") version libs.versions.kotlin
}

group = "appintegrity.maskipli.id"
version = "0.0.1"

sourceSets["main"].resources.srcDir("resources")

application {
    mainClass.set("appintegrity.maskipli.id.ApplicationKt")
}

dependencies {
    implementation(libs.bundles.ktor.server)
    implementation(libs.logback)

    // google auth + integrity
    implementation(libs.google.auth) {
        exclude(group = "com.google.code.findbugs")
    }
    implementation(libs.google.client.jackson) {
        exclude(group = "com.google.code.findbugs")
    }
    implementation(libs.google.app.integrity) {
        exclude(group = "com.google.code.findbugs")
    }

    // test
    testImplementation(libs.ktor.test.host)
    testImplementation(libs.kotlin.junit)
}

tasks {
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "appintegrity.maskipli.id.ApplicationKt"))
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += listOf(
        "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi",
        "-Xopt-in=kotlinx.coroutines.DelicateCoroutinesApi",
        "-Xopt-in=io.ktor.util.InternalAPI"
    )
}
