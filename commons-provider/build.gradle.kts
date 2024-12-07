plugins {
    id("commons.common-conventions")
}

dependencies {
    api(project(":commons-tuple"))
    implementation(project(":commons-collections"))
}