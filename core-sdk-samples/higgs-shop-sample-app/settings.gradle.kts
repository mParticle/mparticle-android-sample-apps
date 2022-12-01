import org.gradle.api.initialization.resolve.RepositoriesMode

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
}

rootProject.name = "Higgs Shop Sample App"
include(":app")
include(":mparticle-android-integration-appsflyer")
project(":mparticle-android-integration-appsflyer").projectDir = File("app/libs/mparticle-android-integration-appsflyer")
