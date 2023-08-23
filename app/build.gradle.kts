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
    buildToolsVersion = "33.0.1"
    defaultConfig {
        applicationId = "com.gals.prayertimes"
        minSdk = 26
        targetSdk = 33
        versionName = "1.0.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        versionCode = 22
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
                isMinifyEnabled = false
                isShrinkResources = false
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
        val coroutinesVersion = "1.6.4"
        val jUnitVersion = "4.13.2"
        val hiltVersion = "2.47"
        val hiltComposeNav = "1.0.0"
        val composeVersion = "1.4.3"
        val composeNavVersion = "2.6.0"
        val composeMaterial3 = "1.1.1"
        val composeActivity = "1.7.2"
        val composeAccompanist = "0.30.1"
        val composeConstraintLayoutVersion = "1.0.1"


        //layouts
        implementation(libs.androidx.appcompat)

        //Retrofit
        implementation(libs.bundles.retrofit)
        ksp(libs.retrofit.moshi.kotlin.codegen)

        //Coroutines
        implementation(libs.kotlinx.coroutines)

        //Hilt
        implementation("com.google.dagger:hilt-android:$hiltVersion")
        implementation("androidx.hilt:hilt-navigation-compose:$hiltComposeNav")
        kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

        //Room
        implementation(libs.bundles.androidx.room)
        kapt(libs.androidx.room.compiler)
        annotationProcessor(libs.androidx.room.compiler)

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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    namespace = "com.gals.prayertimes"

    kapt {
        correctErrorTypes = false
    }
}
