import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "de.jan.natrium"
version = "1.1.2"

plugins {
    kotlin("jvm") version "1.5.21"
    id("io.gitlab.arturbosch.detekt").version("1.18.0")
    id("maven-publish")
}

repositories {
    maven("https://m2.dv8tion.net/releases")
    maven("https://jitpack.io")
    mavenCentral()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group.toString()
            artifactId = "Natrium"
            version = version

            from(components["kotlin"])
        }
    }
}

detekt {
    toolVersion = "1.18.0"
    config = files("config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}


dependencies {
    implementation("net.dv8tion:JDA:4.3.0_310")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("com.sedmelluq:lavaplayer:1.3.77")
    implementation("org.json:json:20210307")
    implementation("com.github.jan-tennert:Translatable:1.1")
}
