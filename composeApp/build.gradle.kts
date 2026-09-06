import co.touchlab.skie.configuration.SealedInterop
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.gms)
    alias(libs.plugins.kotlinSerialization)

    alias(libs.plugins.skie)

}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true

            export(project(":feature:Clipboard"))
        }
    }
    
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)

            implementation(project.dependencies.platform(libs.google.firebase.bom))
            implementation(libs.google.firebase.database)
            implementation(libs.google.firebase.auth)

            implementation(libs.koin.android)
        }

        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.gitlive.firebase.database)
            implementation(libs.gitlive.firebase.auth)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.multiplatform.settings.no.arg)
            implementation(libs.napier)

            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.jetbrains.lifecycle.viewmodelNavigation3)

            implementation(project(":core:UI"))
            implementation(project(":core:Navigation"))
            implementation(project(":core:DataUtils"))
            implementation(project(":core:Cache"))
            api(project(":feature:Clipboard"))
            implementation(project(":feature:Auth"))
            implementation(project(":FirebaseApi"))
            implementation(project(":FirebaseImpl"))


        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.koin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

android {
    namespace = "com.example.sharedclipboard"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.sharedclipboard"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.example.sharedclipboard.MainKt"

        // The default JVM ergonomics on developer machines reserve a multi-gigabyte
        // heap and start with a comparatively large committed heap. This is a small,
        // mostly idle desktop utility, so prefer a low-footprint configuration.
        jvmArgs(
            "-Xms32m",
            "-Xmx384m",
            "-Xss1m",
            "-XX:TieredStopAtLevel=1",
            "-XX:+UseSerialGC",
            "-XX:MinHeapFreeRatio=10",
            "-XX:MaxHeapFreeRatio=20",
            "-XX:ReservedCodeCacheSize=64m"
        )

        buildTypes.release.proguard {
            isEnabled.set(false)
        }

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Shared Clipboard"
            packageVersion = "1.0.0"
            modules("java.sql")

            macOS {
                bundleID = "com.example.sharedclipboard"
                iconFile.set(project.file("src/jvmMain/resources/shared-clipboard.icns"))
            }
        }
    }
}
