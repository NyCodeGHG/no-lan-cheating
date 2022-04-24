plugins {
    id("fabric-loom") version "0.7-SNAPSHOT"
    id("com.modrinth.minotaur") version "1.2.1"
}

group = "de.nycode"
version = ProjectVersion.version

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net")
}

val jvmTarget = 8

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(jvmTarget))
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
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        if (jvmTarget >= 9) {
            options.release.set(jvmTarget)
        }
    }
    create<com.modrinth.minotaur.TaskModrinthUpload>("publishModrinth") {
        token = System.getenv("MODRINTH_TOKEN") ?: findProperty("modrinthToken").toString()
        projectId = "i5JxLPkx"
        versionNumber = project.version.toString()
        uploadFile = remapJar.get().outputs.files.asPath
        addGameVersion(minecraftVersion)
        // Add Fabric API as dependency
        addDependency("J6yPQoBy", com.modrinth.minotaur.request.Dependency.DependencyType.REQUIRED)
    }
}
