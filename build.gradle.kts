import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.21"
    id("io.gitlab.arturbosch.detekt").version("1.18.0")
}

group = "de.jan.natrium"
version = "1.0-SNAPSHOT"

repositories {
    maven {
        name = "m2-dv8tion"
        url = uri("http://m2.dv8tion.net/releases")
        isAllowInsecureProtocol = true
    }
    mavenCentral()
}

detekt {
    toolVersion = "1.18.0"
    config = files("config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}


dependencies {
    implementation("net.dv8tion:JDA:4.3.0_307")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}