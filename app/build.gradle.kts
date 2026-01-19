import com.android.build.api.dsl.ApplicationExtension

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.com.google.dagger.hilt.android)
    alias(libs.plugins.org.jetbrains.compose.compiler)
    alias(libs.plugins.com.google.ksp)
    alias(libs.plugins.org.jetbrains.kotlin.serialization)
    alias(libs.plugins.google.gms.services)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

configure<ApplicationExtension> {
    namespace = "com.gals.prayertimes"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.gals.prayertimes"
        minSdk = 26
        targetSdk = 36
        versionName = "1.1.1"
        versionCode = 30
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", "\"http://prayersapi.scienceontheweb.net/\"")
    }

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
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
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
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }
}

dependencies {
    //Appcompat
    implementation(libs.androidx.appcompat)

    //Retrofit
    implementation(libs.bundles.retrofit)

    //kotlinx
    implementation(libs.bundles.kotlinx)

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

    // Navigation 3
    implementation(libs.bundles.androidx.navigation3)

    //Test
    testImplementation(libs.bundles.unit.test)
}
