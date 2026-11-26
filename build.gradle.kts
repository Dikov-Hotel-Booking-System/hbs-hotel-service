import java.security.KeyPairGenerator
import java.util.Base64

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
val openApiPublicKey = layout.buildDirectory.file("openapi/jwt-public-key.pem")

val generateOpenApiPublicKey by tasks.registering {
	outputs.file(openApiPublicKey)

	doLast {
		val generator = KeyPairGenerator.getInstance("RSA")
		generator.initialize(2048)
		val publicKey = generator.generateKeyPair().public.encoded
		val encodedKey = Base64.getMimeEncoder(64, "\n".toByteArray()).encodeToString(publicKey)

		val keyFile = openApiPublicKey.get().asFile
		keyFile.parentFile.mkdirs()
		keyFile.writeText("-----BEGIN PUBLIC KEY-----\n$encodedKey\n-----END PUBLIC KEY-----\n")
	}
}

tasks.named("forkedSpringBootRun") {
	dependsOn(generateOpenApiPublicKey)
}

openApi {
	apiDocsUrl.set("http://127.0.0.1:$openApiPort/v3/api-docs.yaml")
	outputDir.set(layout.projectDirectory.dir("docs").asFile)
	outputFileName.set("openapi.yaml")
	waitTimeInSeconds.set(30)
	customBootRun {
		args.set(openApiPublicKey.map { keyFile ->
			listOf(
				"--server.address=127.0.0.1",
				"--server.port=$openApiPort",
				"--spring.config.additional-location=optional:$openApiConfig",
				"--spring.security.oauth2.resourceserver.jwt.public-key-location=${keyFile.asFile.toURI()}",
				"--eureka.client.enabled=false"
			)
		})
	}
}

tasks.bootJar {
	archiveFileName.set("app.jar")
}

tasks.jar {
	enabled = false
}
