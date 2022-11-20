import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.seed"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:4.6.6")
    implementation("io.javalin:javalin-openapi:4.6.6")
    implementation("io.javalin:javalin-testtools:4.6.6")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.3")
    implementation("ch.qos.logback:logback-classic:1.4.4")

    // Uncomment if HTTP/2 and ALPN (application layer protocol negotiation) are necessary
    //implementation("org.eclipse.jetty.http2:http2-server:9.4.48.v20220622")
    //implementation("org.eclipse.jetty:jetty-alpn-conscrypt-server:9.4.48.v20220622")

    // Arch unit
    testImplementation("com.tngtech.archunit:archunit:1.0.0")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.0.0")

    testImplementation("org.mockito:mockito-core:4.8.0")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Werror")
        jvmTarget = "11"
        allWarningsAsErrors = true
    }
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveBaseName.set("shadow")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "com.seed.MainKt"))
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}
