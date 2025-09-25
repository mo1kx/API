plugins {
    application
    kotlin("jvm") version "1.8.20"
    id("io.ktor.plugin") version "2.3.0"
}

group = "com.mycompany"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.mycompany.server.ApplicationKt")
}

repositories {
    mavenCentral()
}

val ktorVersion = "2.3.0"
val logbackVersion = "1.4.5"
val serializationVersion = "1.5.1"

dependencies {
    // Основной Ktor-сервер с поддержкой ядра Netty
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")

    // Поддержка Content Negotiator для преобразования объектов в JSON
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

    // Стандартный логгер
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
}