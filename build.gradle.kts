plugins {
    id("fabric-loom") version "0.7-SNAPSHOT"
    `maven-publish`
}

group = "de.nycode"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net")
}

val jvmTarget = 8

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(jvmTarget))
        vendor.set(JvmVendorSpec.ADOPTOPENJDK)
    }
    withSourcesJar()
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnMappingsVersion")
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modRuntime("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
}

tasks {
    processResources {
        from(sourceSets.main.get().resources.srcDirs) {
            filesMatching("fabric.mod.json") {
                expand(
                    "version" to project.version
                )
            }
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        if (jvmTarget >= 9) {
            options.release.set(jvmTarget)
        }
    }
}
