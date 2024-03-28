plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.bitcamp.nc4_all"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bitcamp.nc4_all"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.google.android.material:material:1.0.0-rc01")
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
    testImplementation(libs.junit)
}