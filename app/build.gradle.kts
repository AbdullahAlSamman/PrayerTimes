plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    signingConfigs {
        create("release") {
        }
    }
    compileSdk = 33
    buildToolsVersion = "31.0.0"
    defaultConfig {
        applicationId = "com.gals.prayertimes"
        minSdk = 26
        targetSdk = 33
        versionName = "0.9.8.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        versionCode = 19
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
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
        val okHttpVersion = "4.9.3"
        val appCompatVersion = "1.4.2"
        val recyclerViewVersion = "1.2.1"
        val constraintLayoutViewVersion = "2.1.4"
        val jUnitVersion = "4.13.2"
        val hiltVersion = "2.44"


        implementation("androidx.appcompat:appcompat:$appCompatVersion")
        implementation("androidx.recyclerview:recyclerview:$recyclerViewVersion")
        implementation("androidx.constraintlayout:constraintlayout:$constraintLayoutViewVersion")

        implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleViewModelVersion")

        implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
        implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
        implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")

        implementation("com.squareup.moshi:moshi:$moshiVersion")
        implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
        kapt("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

        implementation("com.google.dagger:hilt-android:$hiltVersion")
        kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

        implementation("androidx.room:room-runtime:$roomVersion")
        kapt("androidx.room:room-compiler:$roomVersion")
        annotationProcessor("androidx.room:room-compiler:$roomVersion")

        testImplementation("junit:junit:$jUnitVersion")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kapt {
        correctErrorTypes = true
    }
}
