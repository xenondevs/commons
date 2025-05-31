plugins {
    id("commons.common-conventions")
    id("commons.dokka-conventions")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xmulti-dollar-interpolation")
    }
}