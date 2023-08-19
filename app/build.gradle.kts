@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    signingConfigs {
        create("release") {
        }
    }
    compileSdk = 33
    buildToolsVersion = "33.0.0"
    defaultConfig {
        applicationId = "com.gals.prayertimes"
        minSdk = 26
        targetSdk = 33
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        versionCode = 20
        vectorDrawables {
            useSupportLibrary = true
        }
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
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
            compose = true
        }
    }

    dependencies {
        val roomVersion = "2.5.2"
        val lifecycleVersion = "2.2.0"
        val lifecycleViewModelVersion = "2.6.1"
        val retrofitVersion = "2.9.0"
        val coroutinesVersion = "1.6.4"
        val moshiVersion = "1.14.0"
        val okHttpVersion = "4.10.0"
        val appCompatVersion = "1.6.1"
        val recyclerViewVersion = "1.3.0"
        val constraintLayoutViewVersion = "2.1.4"
        val jUnitVersion = "4.13.2"
        val hiltVersion = "2.47"
        val hiltComposeNav = "1.0.0"
        val composeVersion = "1.4.3"
        val composeNavVersion = "2.6.0"
        val composeMaterial3 = "1.1.1"
        val composeActivity = "1.7.2"
        val composeAccompanist = "0.30.1"
        val composeConstraintLayoutVersion = "1.0.1"


        //Lifecycle
        implementation("androidx.appcompat:appcompat:$appCompatVersion")
        implementation("androidx.recyclerview:recyclerview:$recyclerViewVersion")
        implementation("androidx.constraintlayout:constraintlayout:$constraintLayoutViewVersion")

        implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleViewModelVersion")

        //Retrofit
        implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
        implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
        implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")

        implementation("com.squareup.moshi:moshi:$moshiVersion")
        implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
        ksp("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")

        //Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

        //Hilt
        implementation("com.google.dagger:hilt-android:$hiltVersion")
        implementation("androidx.hilt:hilt-navigation-compose:$hiltComposeNav")
        kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

        //Room
        implementation("androidx.room:room-runtime:$roomVersion")
        implementation("androidx.room:room-ktx:$roomVersion")
        ksp("androidx.room:room-compiler:$roomVersion")
        annotationProcessor("androidx.room:room-compiler:$roomVersion")

        //Compose
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
        implementation("androidx.activity:activity-compose:$composeActivity")
        implementation("androidx.compose.ui:ui:$composeVersion")
        implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
        implementation("androidx.compose.material3:material3:$composeMaterial3")
        implementation("com.google.accompanist:accompanist-systemuicontroller:$composeAccompanist")
        implementation("com.google.accompanist:accompanist-webview:$composeAccompanist")
        debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
        debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")

        //Compose navigation
        implementation("androidx.navigation:navigation-compose:$composeNavVersion")

        //Compose constraint layout
        implementation("androidx.constraintlayout:constraintlayout-compose:$composeConstraintLayoutVersion")

        //Test
        testImplementation("junit:junit:$jUnitVersion")
        androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    namespace = "com.gals.prayertimes"

    kapt {
        correctErrorTypes = false
    }
}
