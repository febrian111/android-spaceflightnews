plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.navigationSafeargs)
}

android {
    namespace = "test.febri.spaceflightnews"
    compileSdk = 35

    defaultConfig {
        applicationId = "test.febri.spaceflightnews"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
//        manifestPlaceholders = [auth0Domain: "@string/com_auth0_domain", auth0Scheme: "@string/com_auth0_scheme"]

        manifestPlaceholders ["auth0Domain"] = "@string/com_auth0_domain"
        manifestPlaceholders ["auth0Scheme"] = "@string/com_auth0_scheme"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.swiperefreshlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.hilt)
    implementation(libs.hilt.worker)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.glide)
    implementation(libs.paging)
    implementation(libs.facebook.shimmer)
    implementation(libs.timber)
    implementation(libs.threeten)
    implementation(libs.workmanager)
    implementation(libs.chucker)
    implementation(libs.auth0)

    releaseImplementation(libs.chucker.noop)

    ksp (libs.hilt.compiler)
    annotationProcessor(libs.glide.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}