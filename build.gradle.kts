plugins {
    id("fabric-loom") version "0.12-SNAPSHOT"
    id("org.quiltmc.quilt-mappings-on-loom") version "4.2.0"
    id("com.modrinth.minotaur") version "2.1.2"
}

group = "de.nycode"
version = "1.5.0+$minecraftVersion"

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net")
}

val jvmTarget = 17

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(jvmTarget))
    }
    withSourcesJar()
}

val modIncludeImplementation by configurations.creating

configurations {
    include {
        extendsFrom(modIncludeImplementation)
    }
    modImplementation {
        extendsFrom(modIncludeImplementation)
    }
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.layered {
        addLayer(quiltMappings.mappings("org.quiltmc:quilt-mappings:1.19-pre1+build.1:v2"))
        officialMojangMappings()
    })
    modImplementation("net.fabricmc:fabric-loader:0.14.5")
    modIncludeImplementation(fabricApi.module("fabric-resource-loader-v0", "0.52.4+1.19"))
}

tasks {
    processResources {
        from(sourceSets.main.get().resources.srcDirs) {
            filesMatching("fabric.mod.json") {
                expand("version" to project.version)
            }
        }
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        if (jvmTarget >= 9) {
            options.release.set(jvmTarget)
        }
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN") ?: "")
    projectId.set("i5JxLPkx")
    uploadFile.set(tasks.remapJar as Any)
    gameVersions.set(listOf(minecraftVersion))
}
