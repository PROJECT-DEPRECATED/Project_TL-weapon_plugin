plugins {
    kotlin("jvm") version "1.4.32"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    `maven-publish`
}

group = properties["pluginGroup"]!!
version = properties["pluginVersion"]!!

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/") {
        name = "sonatype-oss-snapshots"
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("net.kyori:adventure-api:4.7.0")
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
}

tasks {
    javadoc {
        options.encoding = "UTF-8"
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }

    create<Jar>("sourceJar") {
        archiveClassifier.set("source")
        from(sourceSets["main"].allSource)
    }

    processResources {
        filesMatching("*.yml") {
            expand(project.properties)
        }
    }

    shadowJar {
        archiveBaseName.set(project.name)
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}

publishing {
    publications {
        create<MavenPublication>("Greeting_Plugin") {
            artifact(tasks["sourceJar"])
            from(components["java"])
        }
    }
}