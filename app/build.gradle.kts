plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.library"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.library"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    dataBinding {
        enable = true
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Navigation
    val version_of_navigation = "2.5.3"
    implementation("androidx.navigation:navigation-fragment-ktx:${version_of_navigation}")
    implementation("androidx.navigation:navigation-ui-ktx:${version_of_navigation}")

    // Fragments
    val version_of_fragments = "1.8.6"
    implementation("androidx.fragment:fragment-ktx:${version_of_fragments}")

    implementation("com.facebook.shimmer:shimmer:0.5.0")
}