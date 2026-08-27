plugins {
    kotlin("jvm") version "2.4.10"
    `maven-publish`
}

group = "com.hashtag071629"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

publishing {
    repositories {
        mavenLocal()
    }

    publications {
        create<MavenPublication>("library") {
            groupId = group as String
            artifactId = "discord4j-kotlin-extension"
            from(components["java"])
        }
    }
}

dependencies {
    implementation(libs.discord4j.core)
    implementation(libs.kotlinx.coroutines.reactor)
    implementation(libs.reactor.kotlin.extensions)
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(8)
    explicitApi()
}

tasks.test {
    useJUnitPlatform()
}