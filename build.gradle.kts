import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    val kotlinVersion = "1.9.22"
    val ktorVersion = "2.3.9"

    id("java")
    id("io.ktor.plugin") version ktorVersion
    kotlin("multiplatform") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
}

val ktorVersion = "2.3.9"

repositories {
    mavenCentral()
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

kotlin {
    jvmToolchain(21)
    jvm {
        withJava()
        compilations.all {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
            }
        }
    }
    js {
        browser {
            runTask {
                devServer = KotlinWebpackConfig.DevServer(
                    open = false,
                    port = 3000,
                    static = mutableListOf("kotlin"),
                    proxy = mutableMapOf(
                        "/api" to "http://localhost:8080"
                    )
                )
            }
            binaries.executable()
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(ktor("serialization-kotlinx-json"))
            }
        }

        jvmMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.8.1-Beta")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1-Beta")
                implementation("ch.qos.logback:logback-classic:1.4.14")
                implementation(ktor("server-content-negotiation"))
                implementation(ktor("server-core-jvm"))
                implementation(ktor("server-netty-jvm"))
                implementation(ktor("server-config-yaml"))

                implementation(ktor("client-core"))
                implementation(ktor("client-java"))
                implementation(ktor("client-content-negotiation"))
            }
        }

        jsMain {
            dependencies {
                implementation(ktor("client-js"))
                implementation(ktor("client-content-negotiation"))
                implementation(kotlin("stdlib-js"))
                implementation(kotlinx("html:0.10.1"))
                implementation(kotlinx("datetime:0.6.0-RC"))
                implementation(kotlinx("coroutines-core-js:1.8.1-Beta"))

                implementation(npm("chart.js", "4.4.3"))
            }
        }
    }
}

tasks {
    val jvmProcessResources by getting(Copy::class) {
        val jsBrowserDistribution by getting
        from(jsBrowserDistribution) {
            into("static")
        }
    }
}

fun kotlinx(simpleModuleName: String) = "org.jetbrains.kotlinx:kotlinx-$simpleModuleName"
fun ktor(simpleModuleName: String) = "io.ktor:ktor-$simpleModuleName:$ktorVersion"
