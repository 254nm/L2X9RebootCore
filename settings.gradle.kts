rootProject.name = "L2X9RebootCore"

include(":Common", ":Mixins", ":Plugin")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}