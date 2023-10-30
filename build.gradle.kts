@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.BaseExtension
import com.vanniktech.maven.publish.MavenPublishBaseExtension

plugins {
    id("com.android.application") version "8.1.1" apply false
    id("com.android.library") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.vanniktech.maven.publish") version "0.25.3" apply false
    id("org.jetbrains.changelog") version "2.2.0"
}

allprojects {
    group = properties["GROUP"]!!
    version = properties["VERSION_NAME"]!!

    pluginManager.withPlugin("com.android.application") {
        configureAndroidProject()
    }
    pluginManager.withPlugin("com.android.library") {
        configureAndroidProject()
        configureAndroidLibraryPublication()
    }
}

fun Project.configureAndroidProject() {
    extensions.configure<BaseExtension> {
        compileSdkVersion(34)

        defaultConfig {
            minSdk = 21
            targetSdk = 34
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }
}

fun Project.configureAndroidLibraryPublication() {
    apply(plugin = "com.vanniktech.maven.publish")
    extensions.configure<MavenPublishBaseExtension> {
        beforeEvaluate {
            pomFromGradleProperties()
            configure(
                com.vanniktech.maven.publish.AndroidSingleVariantLibrary(
                    variant = "release",
                )
            )
        }
    }
}