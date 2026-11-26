plugins {
	java
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.dependency.management)
}

group = "ru.mephi"
version = "0.0.1-SNAPSHOT"
description = "Hotel and room management service"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(libs.versions.java.get().toInt())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(libs.spring.boot.starter.data.jpa)
	implementation(libs.spring.boot.starter.jdbc)
	implementation(libs.spring.boot.starter.liquibase)
	implementation(libs.spring.boot.starter.security.oauth2.resource.server)
	implementation(libs.spring.boot.starter.validation)
	implementation(libs.spring.boot.starter.webmvc)
	implementation(libs.spring.cloud.starter.netflix.eureka.client)
	implementation(libs.springdoc.openapi.starter.webmvc.ui)
	compileOnly(libs.lombok)
	annotationProcessor(libs.lombok)
	runtimeOnly(libs.postgresql)
	testImplementation(libs.junit.jupiter)
	testImplementation(libs.mockito.junit.jupiter)
	testRuntimeOnly(libs.junit.platform.launcher)
}

dependencyManagement {
	imports {
		val springCloudBom = libs.spring.cloud.bom.get()
		mavenBom("${springCloudBom.group}:${springCloudBom.name}:${springCloudBom.versionConstraint.requiredVersion}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.bootJar {
	archiveFileName.set("app.jar")
}

tasks.jar {
	enabled = false
}
