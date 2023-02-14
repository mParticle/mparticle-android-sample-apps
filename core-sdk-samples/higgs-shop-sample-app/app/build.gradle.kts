import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

plugins {
    id("com.android.application")
    id("org.jlleitschuh.gradle.ktlint") version "11.1.0"
    // id("com.google.gms.google-services")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "com.mparticle.example.higgsshopsampleapp"
        minSdk = 24
        targetSdk = 33
        versionCode = buildVersionCode()
        versionName = "0.14.1-SNAPSHOT"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "HIGGS_SHOP_SAMPLE_APP_KEY", "\"${System.getenv("HIGGS_SHOP_SAMPLE_APP_KEY")}\"")
        buildConfigField("String", "HIGGS_SHOP_SAMPLE_APP_SECRET", "\"${System.getenv("HIGGS_SHOP_SAMPLE_APP_SECRET")}\"")
        buildConfigField("String", "HIGGS_SHOP_FCM_SENDER_ID", "\"${System.getenv("HIGGS_SHOP_FCM_SENDER_ID")}\"")
    }
    buildFeatures {
        dataBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    namespace = "com.mparticle.example.higgsshopsampleapp"
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("androidx.compose.runtime:runtime:1.3.0")
    implementation("androidx.compose.ui:ui:1.3.0")
    implementation("androidx.compose.material:material:1.3.0")
    implementation("androidx.compose.ui:ui-tooling:1.3.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.fragment:fragment:1.5.4")
    implementation("androidx.fragment:fragment-ktx:1.5.4")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("com.google.android.material:material:1.7.0")
    implementation("com.mparticle:android-core:5.49.1")
    implementation("com.google.android.gms:play-services-ads-identifier:18.0.1")

    // implementation(platform("com.google.firebase:firebase-bom:31.0.2"))
    // implementation("com.google.firebase:firebase-analytics-ktx")

    // implementation("com.mparticle:android-media:1.4.2")

    implementation("com.github.bumptech.glide:glide:4.14.2")

    api("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.4")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    debugImplementation("androidx.compose.ui:ui-tooling:1.3.0")

    val roomVersion = "2.4.3"
    implementation("androidx.room:room-runtime:$roomVersion")
    // annotationProcessor("androidx.room:room-compiler:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    // ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-rxjava2:$roomVersion")
    // implementation("androidx.room:room-rxjava3:$roomVersion")
    // implementation("androidx.room:room-guava:$roomVersion")
    // implementation("androidx.room:room-paging:2.4.1")
    testImplementation("androidx.room:room-testing:$roomVersion")
    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.3.0")
    androidTestImplementation("androidx.test:core:1.4.0")
    androidTestImplementation("androidx.test:core-ktx:1.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.3")
    androidTestImplementation("androidx.test.ext:truth:1.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("com.google.truth:truth:1.1.3")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    androidTestUtil("androidx.test:orchestrator:1.4.1")
}

fun buildVersionCode(): Int {
    val df = SimpleDateFormat("yyyyMMddHH")
    df.timeZone = TimeZone.getTimeZone("EST")
    return df.format(Date()).toInt()
}
