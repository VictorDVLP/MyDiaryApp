pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MyDiaryApp"
include(":app")
include(":feature:calendar:domain")
include(":feature:calendar:data")
include(":feature:calendar:framework")
include(":feature:calendar:usecases")
include(":feature:calendar:presentation")
include(":feature:notification")
