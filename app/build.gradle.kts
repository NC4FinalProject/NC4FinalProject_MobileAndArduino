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

    buildFeatures{
        dataBinding = true;
        viewBinding = true;
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
    implementation("com.google.android.material:material:1.4.0")
    
    //Retrofit 관련 라이브러리
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    //로그를 남기기 위한 라이브러리
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
    testImplementation(libs.junit)
}