plugins {
    id("commons.common-conventions")
    id("commons.dokka-conventions")
}

version = "2.0.0-alpha.5"

dependencies {
    api(project(":commons-tuple"))
}