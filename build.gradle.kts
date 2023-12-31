plugins {
	java
	id("org.springframework.boot") version "3.1.5"
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
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("com.h2database:h2:2.2.220")
	implementation("org.hibernate:hibernate-community-dialects:6.3.0.Final")
	implementation("org.modelmapper:modelmapper:2.1.1")
	implementation("org.springframework.security:spring-security-core:6.1.4")
	implementation("org.springframework.security:spring-security-config:6.1.4")
	implementation("org.springframework.security:spring-security-web:6.1.4")
	implementation("io.jsonwebtoken:jjwt-api:0.12.2")
	implementation ("com.fasterxml.jackson.core:jackson-databind:2.10.0")




	compileOnly("org.projectlombok:lombok")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	annotationProcessor("org.projectlombok:lombok")

	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.2")
	runtimeOnly("io.jsonwebtoken:jjwt-orgjson:0.12.2")
	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.hamcrest:hamcrest:2.1")
	testImplementation("org.mockito:mockito-core:3.12.4")
	testImplementation("junit:junit:4.13.1")

	testImplementation("com.h2database:h2:2.2.220")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
