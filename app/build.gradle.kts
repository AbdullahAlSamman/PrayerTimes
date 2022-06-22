plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    /*   signingConfigs {
           Gebetszeiten {
               keyAlias "Gebetszeiten"
               keyPassword "@Gals18"
               storeFile file("D:/Projects/Gebetszeiten/SignKeys/key.jks")
               storePassword "@Gebetszeiten18"
           }
           Debug {
               storeFile file("C:/Users/Genius/.android/debug.keystore")
           }
       }*/
    compileSdk = 30
    buildToolsVersion = "31.0.0"
    defaultConfig {
        applicationId = "com.gals.prayertimes"
        minSdk = 24
        targetSdk = 30
        versionName = "0.9.6"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        /*signingConfig signingConfigs.Gebetszeiten*/
        versionCode = 16
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
            /*signingConfig signingConfigs.Gebetszeiten*/
        }
    }
}

dependencies {
    val roomVersion: String = "2.4.0-alpha03"

    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.1")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    testImplementation("junit:junit:4.13.2")
}
