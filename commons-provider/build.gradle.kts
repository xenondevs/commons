plugins {
    id("commons.common-conventions")
    id("commons.dokka-conventions")
}

dependencies {
    api(project(":commons-tuple"))
    implementation(project(":commons-collections"))
}