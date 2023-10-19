plugins {
	java
	id("org.springframework.boot") version "3.1.3"
	id("io.spring.dependency-management") version "1.1.3"
}

group = "no.haugalandplus"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.xerial:sqlite-jdbc:3.41.2.2")
	//implementation("com.h2database:h2")
	implementation("org.hibernate:hibernate-community-dialects:6.3.0.Final")
	implementation("org.modelmapper:modelmapper:2.1.1")
	implementation("org.springframework.security:spring-security-core:6.1.4")
	implementation("org.springframework.security:spring-security-config:6.1.4")
	implementation("org.springframework.security:spring-security-web:6.1.4")
	testImplementation("junit:junit:4.13.1")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("io.jsonwebtoken:jjwt-api:0.12.2")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.2")
	runtimeOnly("io.jsonwebtoken:jjwt-orgjson:0.12.2")
	testImplementation("org.hamcrest:hamcrest:2.1")
	testImplementation("org.mockito:mockito-core:3.12.4")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
