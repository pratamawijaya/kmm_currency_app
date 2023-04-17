@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application") version libs.versions.android.gradle.plugin.get() apply false
    id("com.android.library") version libs.versions.android.gradle.plugin.get() apply false
    kotlin("android") version libs.versions.kotlin.get() apply false
    kotlin("multiplatform") version libs.versions.kotlin.get() apply false
}

buildscript {
    dependencies {
        classpath("com.squareup.sqldelight:gradle-plugin:${libs.versions.sqlDelight.get()}")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
