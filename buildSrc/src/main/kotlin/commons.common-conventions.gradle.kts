group = "xyz.xenondevs.commons"
version = "1.23"

plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib:2.1.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
}

kotlin {
    compilerOptions { 
        optIn.add("kotlin.contracts.ExperimentalContracts")
    }
}

java {
    withSourcesJar()
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