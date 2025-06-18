plugins {
    id("java")
    id ("org.springframework.boot") version "3.2.0"
    id ("io.spring.dependency-management") version "1.1.0"
}

group = "main.project_1"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-web")
    implementation ("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation ("org.eclipse.jgit:org.eclipse.jgit:6.8.0.202311291450-r")
    implementation ("org.jfree:jfreechart:1.5.4")
    compileOnly ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}