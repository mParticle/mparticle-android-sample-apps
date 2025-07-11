import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

plugins {
    id("com.android.application")
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    // id("com.google.gms.google-services")
    kotlin("android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.mparticle.example.higgsshopsampleapp"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.mparticle.example.higgsshopsampleapp"
        minSdk = 24
        targetSdk = 34
        versionCode = buildVersionCode()
        versionName = "0.14.1-SNAPSHOT"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "HIGGS_SHOP_SAMPLE_APP_KEY", "\"${System.getenv("HIGGS_SHOP_SAMPLE_APP_KEY")}\"")
        buildConfigField("String", "HIGGS_SHOP_SAMPLE_APP_SECRET", "\"${System.getenv("HIGGS_SHOP_SAMPLE_APP_SECRET")}\"")
        buildConfigField("String", "HIGGS_SHOP_FCM_SENDER_ID", "\"${System.getenv("HIGGS_SHOP_FCM_SENDER_ID")}\"")
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        dataBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // AndroidX BOM
    implementation(platform("androidx.compose:compose-bom:2024.01.00"))
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material:material")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.runtime:runtime-livedata")

    // Core AndroidX dependencies
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Coroutines
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.7.3"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    // OkHttp
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // mParticle
    implementation("com.mparticle:android-core:5+")
    implementation("com.mparticle:android-kit-base:5+")
    implementation("com.mparticle:android-rokt-kit:5+")

    // Google Services
    implementation("com.google.android.gms:play-services-ads-identifier:18.0.1")

    // Other dependencies
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    ksp("com.github.bumptech.glide:compiler:4.16.0")

    // Testing dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

fun buildVersionCode(): Int {
    val df = SimpleDateFormat("yyyyMMddHH")
    df.timeZone = TimeZone.getTimeZone("EST")
    return df.format(Date()).toInt()
}
