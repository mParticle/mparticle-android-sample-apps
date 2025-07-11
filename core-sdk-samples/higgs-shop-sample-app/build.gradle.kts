// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.6.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.25")
        classpath("com.google.gms:google-services:4.3.14")
    }
}

plugins {
    id("com.google.devtools.ksp") version "1.9.25-1.0.20" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}