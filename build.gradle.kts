plugins {
    id("fabric-loom") version "1.2-SNAPSHOT"
    id("com.modrinth.minotaur") version "2.8.1"
}

val minecraftVersion = "1.20.1"

group = "de.nycode"

version = "1.5.0+$minecraftVersion"

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net")
    maven("https://maven.parchmentmc.org/")
}

val jvmTarget = 17

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(jvmTarget)) }
    withSourcesJar()
}

val modIncludeImplementation by configurations.creating

configurations {
    include { extendsFrom(modIncludeImplementation) }
    modImplementation { extendsFrom(modIncludeImplementation) }
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.layered {
        parchment("org.parchmentmc.data:parchment-1.20.1:2023.06.26@zip")
        officialMojangMappings()
    })
    modImplementation("net.fabricmc:fabric-loader:0.14.21")
    modIncludeImplementation(fabricApi.module("fabric-resource-loader-v0", "0.83.1+1.20.1"))
}

tasks {
    processResources {
        from(sourceSets.main.get().resources.srcDirs) {
            filesMatching("fabric.mod.json") { expand("version" to project.version) }
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
