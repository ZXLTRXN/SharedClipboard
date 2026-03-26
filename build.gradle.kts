plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.androidLint) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.mockative) apply false
    alias(libs.plugins.skie) apply false
}

subprojects {
    configurations.all {
        if (name.contains("jvm", ignoreCase = true)) {
            resolutionStrategy.eachDependency {
                if (requested.group == "io.github.aakira" && requested.name == "napier") {
                    useTarget("io.github.aakira:napier-jvm:${requested.version}")
                }
            }
        }
    }
}