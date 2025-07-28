import org.gradle.accessors.dm.LibrariesForLibs

group = "xyz.xenondevs.commons"
version = "1.34"

plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm")
}

val libs = the<LibrariesForLibs>()

repositories {
    mavenCentral()
}

dependencies {
    api(libs.kotlin.stdlib)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.platformLauncher)
    testImplementation(libs.kotlin.test.junit)
}

kotlin {
    compilerOptions { 
        optIn.add("kotlin.contracts.ExperimentalContracts")
    }
}

java {
    withSourcesJar()
}

tasks {
    test {
        useJUnitPlatform()
    }
}

publishing {
    repositories {
        maven {
            credentials {
                name = "xenondevs"
                url = uri { "https://repo.xenondevs.xyz/releases/" }
                credentials(PasswordCredentials::class)
            }
        }
    }
    
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}