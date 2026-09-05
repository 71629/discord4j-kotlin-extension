plugins {
    kotlin("jvm") version "2.4.10"
    `maven-publish`
}

val packageVersion = System.getenv("VERSION") ?: "DEVELOPMENT"

group = "com.hashtag071629"
version = packageVersion

repositories {
    mavenCentral()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/71629/discord4j-kotlin-extension")
            credentials {
                username = System.getenv("USERNAME")
                password = System.getenv("TOKEN")
            }
        }
        mavenLocal()
    }

    publications {
        create<MavenPublication>("library") {
            groupId = group as String
            artifactId = "discord4j-kotlin-extension"
            version = packageVersion
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