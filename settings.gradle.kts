include(":app")

rootProject.buildFileName = "build.gradle.kts"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}