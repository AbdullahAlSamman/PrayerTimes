plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.com.google.dagger.hilt.android) apply false
    alias(libs.plugins.org.jetbrains.compose.compiler) apply false
    alias(libs.plugins.com.google.ksp) apply false
    alias(libs.plugins.org.jetbrains.kotlin.serialization) apply false
    alias(libs.plugins.google.gms.services) apply false
    alias (libs.plugins.firebase.crashlaytics) apply false
}
