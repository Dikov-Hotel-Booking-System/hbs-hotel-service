plugins {
	java
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.dependency.management)
	alias(libs.plugins.springdoc.openapi.gradle.plugin)
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

val openApiPort = 18082
val openApiConfig = layout.projectDirectory.file("config/application.yaml").asFile.toURI()

openApi {
	apiDocsUrl.set("http://127.0.0.1:$openApiPort/v3/api-docs.yaml")
	outputDir.set(layout.projectDirectory.dir("docs").asFile)
	outputFileName.set("openapi.yaml")
	waitTimeInSeconds.set(30)
	customBootRun {
		args.set(listOf(
			"--server.address=127.0.0.1",
			"--server.port=$openApiPort",
			"--spring.config.additional-location=optional:$openApiConfig",
			"--eureka.client.enabled=false"
		))
	}
}

tasks.bootJar {
	archiveFileName.set("app.jar")
}

tasks.jar {
	enabled = false
}
