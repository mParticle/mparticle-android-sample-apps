import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

plugins {
    id("com.android.application")
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    kotlin("android")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "com.mparticle.example.higgsshopsampleapp"
        minSdk = 16
        targetSdk = 31
        versionCode = buildVersionCode()
        versionName = "0.1.0-SNAPSHOT"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

fun buildVersionCode(): Int {
    val df = SimpleDateFormat("yyyyMMddHH")
    df.setTimeZone(TimeZone.getTimeZone("EST"))
    return df.format(Date()).toInt()
}
