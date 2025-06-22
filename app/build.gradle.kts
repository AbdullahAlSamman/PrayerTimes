plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    alias(libs.plugins.com.google.dagger.hilt.android)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.compose.compiler)
    alias(libs.plugins.com.google.ksp)
}
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

android {
    namespace = "com.gals.prayertimes"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.gals.prayertimes"
        minSdk = 26
        targetSdk = 35
        versionName = "1.0.7"
        versionCode = 27
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
    }

    kotlinOptions { jvmTarget = "17" }

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
        ksp(libs.retrofit.moshi.kotlin.codegen)

        //Coroutines
        implementation(libs.kotlinx.coroutines)

        //Room
        implementation(libs.bundles.androidx.room)
        ksp(libs.androidx.room.compiler)
        annotationProcessor(libs.androidx.room.compiler)

        //Compose
        implementation(libs.bundles.androidx.compose)
        implementation(libs.bundles.androidx.accompainst)
        debugImplementation(libs.bundles.androidx.compose.tooling)

        //Work manager
        implementation(libs.bundles.androidx.work.manager)

        //Hilt
        implementation(libs.google.dagger.hilt)
        implementation(libs.androidx.hilt)
        ksp(libs.google.dagger.hilt.compiler)

        //Test
        testImplementation(libs.bundles.unit.test)

    }
}
