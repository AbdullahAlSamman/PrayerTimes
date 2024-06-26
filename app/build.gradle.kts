plugins {
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    namespace = "com.gals.prayertimes"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.gals.prayertimes"
        minSdk = 26
        targetSdk = 34
        versionName = "1.0.4"
        versionCode = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

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

        buildFeatures {
            compose = true
        }

        dependenciesInfo {
            includeInApk = true
        }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }

    signingConfigs {
        create("release") {}
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.12"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    kapt {
        correctErrorTypes = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        val hiltVersion = "2.50"
        val hiltComposeNav = "1.2.0"

        //Appcompat
        implementation(libs.androidx.appcompat)

        //Retrofit
        implementation(libs.bundles.retrofit)
        kapt(libs.retrofit.moshi.kotlin.codegen)

        //Coroutines
        implementation(libs.kotlinx.coroutines)

        //Room
        implementation(libs.bundles.androidx.room)
        kapt(libs.androidx.room.compiler)
        annotationProcessor(libs.androidx.room.compiler)

        //Compose
        implementation(libs.bundles.androidx.compose)
        implementation(libs.bundles.androidx.accompainst)
        debugImplementation(libs.bundles.androidx.compose.tooling)

        //Hilt
        implementation("com.google.dagger:hilt-android:$hiltVersion")
        implementation("androidx.hilt:hilt-navigation-compose:$hiltComposeNav")
        kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

        //Firebase
        implementation(platform(libs.firebase.bom))
        implementation(libs.bundles.firebase)

        //Test
        androidTestImplementation(libs.androidx.compose.junit4.test)
        testImplementation(libs.junit.test)
    }
}
