import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ktlint)
    // id("com.google.gms.google-services")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.mparticle.example.higgsshopsampleapp"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.mparticle.example.higgsshopsampleapp"
        minSdk = 24
        targetSdk = 35
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    lint {
        baseline = file("lint-baseline.xml")
        disable += "MParticleVersionInconsistency"
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    // AndroidX BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material.material)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.runtime.livedata)

    // Core AndroidX dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recyclerview)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Coroutines
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.org.jetbrains.kotlinx.coroutines.android)
    implementation(libs.org.jetbrains.kotlinx.coroutines.core)

    // OkHttp
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp3.okhttp)
    implementation(libs.squareup.logging.interceptor)

    // Retrofit
    implementation(libs.com.squareup.retrofit)
    implementation(libs.com.squareup.retrofit.converter.gson)

    // mParticle — bounded upper bound prevents resolving to 6.0.0-rc.1+ on
    // Maven Central, which removed deprecated symbols. See mparticle-android-sdk#710.
    implementation("com.mparticle:android-core:[5.0,6.0)")
    implementation("com.mparticle:android-kit-base:[5.0,6.0)")
    implementation("com.mparticle:android-rokt-kit:[5.0,6.0)")

    // Google Services
    implementation(libs.play.services.ads.identifier)

    // Other dependencies
    implementation(libs.material)
    implementation(libs.gson)
    implementation(libs.glide)
    ksp(libs.compiler.glide)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.junit.ktx)
    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.core)
    androidTestImplementation(libs.core.ktx)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.truth)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.mockito.kotlin)
}

fun buildVersionCode(): Int {
    val df = SimpleDateFormat("yyyyMMddHH")
    df.timeZone = TimeZone.getTimeZone("EST")
    return df.format(Date()).toInt()
}
