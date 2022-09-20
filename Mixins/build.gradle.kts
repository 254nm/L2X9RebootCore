plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "me.l2x9"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://repo.txmc.me/releases") }
}

dependencies {
    implementation("me.txmc:rtmixin:1.5.1-BETA")
    implementation(project(":Common"))
    compileOnly("com.destroystokyo.paper:paper-jar:1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
}
tasks.shadowJar {
    manifest {
        attributes("Manifest-Version" to "1.0", "Premain-Class" to "me.txmc.rtmixin.jagent.AgentMain", "Agent-Class" to "me.txmc.rtmixin.jagent.AgentMain", "Can-Redefine-Classes" to "true", "Can-Retransform-Classes" to "true", "Can-Set-Native-Method-Prefix" to "true")
    }
    doLast {
        val pluginProj = project(":Plugin")
        val pluginResources = File(pluginProj.projectDir, "src/main/resources")
        val mixinLibs = File(project.buildDir, "libs")
        pluginResources.listFiles()?.filter { it.name.equals("mixins.dat") }?.forEach { it.delete() }
        mixinLibs.listFiles()?.filter { fi -> fi.name.endsWith("-all.jar") }?.forEach { //Copy the new file over
            val newFile = File(pluginResources, "mixins.dat")
            if (newFile.exists()) {
                newFile.delete()
                println("Deleted old mixin file ${newFile.name}")
            }
            it.copyTo(newFile, true)
            println("Copied a file ${newFile.absolutePath}")
        }
    }
}