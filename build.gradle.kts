plugins {
    id("fabric-loom")
    val kotlinVersion: String by System.getProperties()
    kotlin("jvm").version(kotlinVersion)
    kotlin("plugin.serialization") version "1.6.10"
}
base {
    val archivesBaseName: String by project
    archivesName.set(archivesBaseName)
}
val modVersion: String by project
version = modVersion
val mavenGroup: String by project
group = mavenGroup
minecraft {}
repositories {
    maven {
        name = "Ladysnake Mods"
        url = uri("https://ladysnake.jfrog.io/artifactory/mods")
    }

    maven {
        name = "CottonMC"
        url = uri("https://server.bbkr.space/artifactory/libs-release")
    }
    maven {
        url = uri("https://maven.shedaniel.me/")
    }
    maven {
        url = uri("https://maven.terraformersmc.com/")
    }
    maven {
        url = uri("https://maven.nucleoid.xyz/")
    }
    maven {
        url = uri("https://www.cursemaven.com")

        content {
            includeGroup("curse.maven")
        }
    }

    mavenLocal()
}
dependencies {
    val minecraftVersion: String by project
    minecraft("com.mojang:minecraft:$minecraftVersion")
    val yarnMappings: String by project
    mappings("net.fabricmc:yarn:$yarnMappings:v2")
    val loaderVersion: String by project
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    val fabricVersion: String by project
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
    val fabricKotlinVersion: String by project
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")

    modImplementation("io.github.onyxstudios.Cardinal-Components-API:cardinal-components-base:4.0.1")
    modImplementation("io.github.onyxstudios.Cardinal-Components-API:cardinal-components-item:4.0.1")
    modImplementation("io.github.onyxstudios.Cardinal-Components-API:cardinal-components-entity:4.0.1")
    modImplementation("io.github.natanfudge:fabric-drawer:4.1.0-1.17.1")
    //modImplementation("curse.maven:dataattributes-514734:3605221") // 1.1.3
    modImplementation("com.github.clevernucleus:DataAttributes:1.1.4")

    modImplementation(include("eu.pb4:placeholder-api:1.1.3+1.17.1")!!)

    //modImplementation(include("com.github.clevernucleus:PlayerEx:3.1.0")!!)

    modImplementation(include("io.github.cottonmc:LibGui:5.3.0+1.18")!!)
}
tasks {
    val javaVersion = JavaVersion.VERSION_17
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        options.release.set(javaVersion.toString().toInt())
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions { jvmTarget = javaVersion.toString() }
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
    }
    jar { from("LICENSE") { rename { "${it}_${base.archivesName}" } } }
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") { expand(mutableMapOf("version" to project.version)) }
    }
    java {
        toolchain { languageVersion.set(JavaLanguageVersion.of(javaVersion.toString())) }
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        withSourcesJar()
    }
}