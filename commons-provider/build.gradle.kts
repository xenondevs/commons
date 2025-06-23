plugins {
    id("commons.common-conventions")
    id("commons.dokka-conventions")
}

version = "2.0.0-alpha.4"

dependencies {
    api(project(":commons-tuple"))
    implementation(project(":commons-collections"))
}