plugins {
    id("commons.dokka-conventions")
}

dependencies {
    dokka(project(":commons-collections"))
    dokka(project(":commons-gson"))
    dokka(project(":commons-guava"))
    dokka(project(":commons-reflection"))
    dokka(project("commons-tuple"))
    dokka(project("commons-version"))
}