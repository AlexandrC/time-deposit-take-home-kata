import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
	alias(libs.plugins.kotlin.jvm) apply false
	alias(libs.plugins.kotlin.spring) apply false
	alias(libs.plugins.kotlin.jpa) apply false
	alias(libs.plugins.spring.boot) apply false
}
allprojects {
	group = "org.ikigaidigital"
	version = "0.0.1-SNAPSHOT"
}

subprojects {
	repositories { mavenCentral() }

	plugins.withId("org.jetbrains.kotlin.jvm") {
		extensions.configure<KotlinJvmProjectExtension> {
			jvmToolchain(25)
			compilerOptions {
				freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
			}
		}
	}
	tasks.withType<Test> { useJUnitPlatform() }
}
