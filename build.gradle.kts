plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.txmc.me/releases")
        url = uri("https://oss.sonatype.org/content/groups/public/")
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("me.txmc:protocolapi:1.1-SNAPSHOT_1.12.2")
    compileOnly("com.destroystokyo.paper:paper-jar:1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
}

tasks.shadowJar {
    relocate("me.txmc", "me.l2x9")
    exclude("pom.*")
    minimize()
}

group = "me.l2x9"
version = "1.0-SNAPSHOT"
description = "L2X9RebootCore"
java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
