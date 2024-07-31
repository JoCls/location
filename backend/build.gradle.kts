plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.spring") version "1.8.0"
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
}

group = "be.jocls"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("com.sun.xml.bind:jaxb-core:2.3.0.1")
    implementation("com.sun.xml.bind:jaxb-impl:2.3.1")

    implementation("javax.validation:validation-api:2.0.1.Final")

    implementation("mysql:mysql-connector-java:8.0.28")

    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    implementation("org.modelmapper:modelmapper:3.1.0")


    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
