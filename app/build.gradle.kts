plugins {
    id("com.android.application")
    alias(libs.plugins.com.google.dagger.hilt.android)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.compose.compiler)
    alias(libs.plugins.com.google.ksp)
    alias(libs.plugins.org.jetbrains.kotlin.serialization)
}
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

android {
    namespace = "com.gals.prayertimes"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.gals.prayertimes"
        minSdk = 26
        targetSdk = 36
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
            debug {
                isMinifyEnabled = false
                isShrinkResources = false
                isDebuggable = true
                isJniDebuggable = true
                applicationIdSuffix = ".debug"
            }
        }

        buildFeatures {
            compose = true
            buildConfig = true
        }

        dependenciesInfo { includeInApk = false }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    kotlin {
        jvmToolchain(21)
        compilerOptions.freeCompilerArgs.add("-Xannotation-default-target=param-property")
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

        //Compose
        implementation(libs.bundles.androidx.compose)
        implementation(libs.bundles.androidx.accompainst)
        debugImplementation(libs.bundles.androidx.compose.tooling)

        //Work manager
        implementation(libs.bundles.androidx.work.manager)

        //Hilt
        implementation(libs.bundles.hilt)
        ksp(libs.google.dagger.hilt.compiler)

        //Logging
        implementation(libs.timber)

        //Test
        testImplementation(libs.bundles.unit.test)
    }
}
