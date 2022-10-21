group = "me.l2x9"
version = "1.0-SNAPSHOT"
description = "L2X9RebootCore"
java.sourceCompatibility = JavaVersion.VERSION_11

plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://repo.txmc.me/releases") }
}

dependencies {
    implementation("me.txmc:protocolapi:1.2-SNAPSHOT")
    compileOnly("com.destroystokyo.paper:paper-jar:1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
}

tasks.shadowJar {
    relocate("me.txmc", "me.l2x9")
    exclude("pom.*")
    minimize()
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
