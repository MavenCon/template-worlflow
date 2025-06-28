plugins {
    kotlin("multiplatform") version "2.1.21"
}

dependencies {
    commonMainImplementation(kotlin("stdlib-js"))
    commonMainImplementation("org.jetbrains.kotlinx:kotlinx-html-js:0.8.0")
}

kotlin {
    js {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
        binaries.executable()
    }
}