plugins {
    id("fabric-loom") version "1.12-SNAPSHOT"
    id("com.modrinth.minotaur") version "2.8.10"
}

val minecraftVersion = "1.21.1"

group = "de.nycode"

version = "1.5.0+$minecraftVersion"

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net")
}

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(21)) }
    withSourcesJar()
}

val modIncludeImplementation by configurations.creating

configurations {
    include { extendsFrom(modIncludeImplementation) }
    modImplementation { extendsFrom(modIncludeImplementation) }
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.17.3")
    modIncludeImplementation(fabricApi.module("fabric-resource-loader-v0", "0.116.7+1.21.1"))
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
        options.release.set(21)
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN") ?: "")
    projectId.set("i5JxLPkx")
    uploadFile.set(tasks.remapJar as Any)
    gameVersions.set(listOf(minecraftVersion))
}
