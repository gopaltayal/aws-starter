import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.6.20"
    kotlin("jvm") version "1.8.10"
}

group = "com.kreuzwerker"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

extra["testcontainersVersion"] = "1.17.6"

dependencies {
    // Core
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(kotlin("stdlib-jdk8"))
        // Without this the Spring Boot Stereotypes don't work with Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Swagger openAPI
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.3")
        // Needed for validating Java Beans, without it controllers won't be validated.
    implementation("org.springframework.boot:spring-boot-starter-validation:3.0.4")

    // Needed for JSON serialisation in Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")


    // Database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        // Without this the database driver is not found
    implementation("mysql:mysql-connector-java:8.0.32")
    implementation("org.liquibase:liquibase-core")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")
    testImplementation("org.mockito:mockito-core:5.2.0")
    testImplementation("com.h2database:h2:2.1.214")
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

noArg {
    // generates a no-arg constructor for all classes annotated with the following annotation
    annotation("jakarta.persistence.Entity")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}