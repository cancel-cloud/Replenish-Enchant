import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.4.10"
    id("com.gradleup.shadow") version "9.6.1"
}

group = "de.cancelcloud"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://repo.purpurmc.org/snapshots")
}

dependencies {
    compileOnly("org.purpurmc.purpur:purpur-api:26.3.build.2656-stable")
    // Provided at runtime by MCKotlin-Paper. The bootstrapper is Java, so it does not need this.
    compileOnly(kotlin("stdlib"))
}

tasks {
    shadowJar {
        archiveBaseName.set("PurpurStats")
        archiveClassifier.set("")
        archiveVersion.set(version.toString())
    }
}

kotlin {
    jvmToolchain(25)
}

tasks.processResources {
    filesMatching("paper-plugin.yml") {
        expand("version" to project.version)
    }
}

java {
    withSourcesJar()
}

// Konfiguration des ShadowJar-Tasks:
// Dieser Task erstellt die "geshadete" Jar, in der neben deinen Klassen auch alle notwendigen Abhängigkeiten (z. B. kotlin-stdlib) enthalten sind.
tasks.named<ShadowJar>("shadowJar").configure {
    archiveBaseName.set("ReplenishEnchant")
    archiveClassifier.set("")
    manifest {
        // Hier setzen wir das Main-Class-Attribut:
        attributes(mapOf("Main-Class" to "cancelcloud.ReplenishEnchantPlugin"))
    }
}

// Task zum Kopieren des erstellten JAR in den Zielordner:
tasks.register<Copy>("copyJar") {
    dependsOn(tasks.named<ShadowJar>("shadowJar"))
    from(tasks.named<ShadowJar>("shadowJar").get().archiveFile.get().asFile)
    into(file("/Users/cancelcloud/Developer/Minecraft/purpur26-3/plugins/"))
    rename { "Replenish-${version}.jar" }
}

// Finalisiere den Build-Task, sodass nach dem Build automatisch das JAR kopiert wird:
tasks.named("build") {
    finalizedBy("copyJar")
}