import com.modrinth.minotaur.dependencies.ModDependency

plugins {
    id("fabric-loom") version "0.12-SNAPSHOT"
    id("com.modrinth.minotaur") version "2.1.2"
}

group = "de.nycode"
version = ProjectVersion.version

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net")
}

val jvmTarget = 16

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(jvmTarget))
    }
    withSourcesJar()
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.14.5")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.46.1+1.17")
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
    // Add Fabric API as dependency
    dependencies.set(listOf(ModDependency("P7dR8mSH", "required")))
}
