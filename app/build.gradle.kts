plugins {
    id("com.android.application")
    alias(libs.plugins.com.google.dagger.hilt.android)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    alias(libs.plugins.org.jetbrains.compose.compiler)
}

android {
    namespace = "com.gals.prayertimes"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.gals.prayertimes"
        minSdk = 29
        targetSdk = 34
        versionName = "1.0.5"
        versionCode = 25
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        signingConfigs {
            create("release") {
                keyAlias = System.getenv("RELEASE_KEY_ALIAS")
                keyPassword = System.getenv("RELEASE_KEY_PASSWORD")
                storeFile = file("../keystore.jks")
                storePassword = System.getenv("RELEASE_STORE_PASSWORD")
            }
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
                signingConfig = signingConfigs.getByName("release")
            }
        }

        buildFeatures { compose = true }

        dependenciesInfo { includeInApk = false }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }

    kotlinOptions { jvmTarget = "17" }

    composeOptions { kotlinCompilerExtensionVersion = "1.5.15" }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    dependencies {
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

        //Test
        testImplementation(libs.bundles.unit.test)

        //Hilt
        implementation(libs.google.dagger.hilt)
        implementation(libs.androidx.hilt)
        kapt(libs.google.dagger.hilt.compiler)
    }
}
