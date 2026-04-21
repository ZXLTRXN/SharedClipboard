import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)

    alias(libs.plugins.gms)
    alias(libs.plugins.kotlinSerialization)

    alias(libs.plugins.mockative)
}

kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    androidLibrary {
        namespace = "com.example.firebaseimpl"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }


    val xcfName = "FirebaseImplKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlinx.coroutines.core)

                implementation(libs.gitlive.firebase.auth)
                implementation(libs.gitlive.firebase.database)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)

                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)

                implementation(libs.napier)

                implementation(libs.multiplatform.settings.no.arg)

                implementation(libs.sqldelight.coroutines.extensions)

                implementation(project(":FirebaseApi"))
                implementation(project(":core:DataUtils"))
                implementation(project(":core:Cache"))

            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.koin.test)
                implementation(libs.turbine)
                implementation(libs.mockative)
            }
        }


        androidMain {
            dependencies {
                implementation(project.dependencies.platform(libs.google.firebase.bom))
                implementation(libs.google.firebase.database)
                implementation(libs.google.firebase.auth)
            }
        }

        getByName("androidHostTest") {
            dependencies {
                implementation(libs.sqldelight.jvm)
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.testExt.junit)
            }
        }

        iosMain {
            dependencies {
            }
        }
    }

}