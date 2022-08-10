plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    signingConfigs {
        create("release") {
        }
    }
    compileSdk = 31
    buildToolsVersion = "31.0.0"
    defaultConfig {
        applicationId = "com.gals.prayertimes"
        minSdk = 26
        targetSdk = 31
        versionName = "0.9.6"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        versionCode = 16
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = false
            isJniDebuggable = false
        }
    }
    dependenciesInfo {
        includeInApk = true
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true

    }
}

dependencies {
    val roomVersion = "2.4.1"
    val lifecycleVersion = "2.2.0"
    val lifecycleViewModelVersion = "2.4.1"
    val retrofitVersion = "2.9.0"
    val coroutinesVersion = "1.6.1"
    val moshiVersion = "1.13.0"


    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleViewModelVersion")

    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")

    implementation("com.squareup.moshi:moshi:$moshiVersion")
    implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    testImplementation("junit:junit:4.13.2")
}
