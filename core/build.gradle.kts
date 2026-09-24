plugins {
	alias(libs.plugins.kotlin.jvm)
}

dependencies {
	testImplementation(platform(libs.spring.boot.bom))
	testImplementation(libs.junit.jupiter)
	testImplementation(libs.assertj.core)
	testRuntimeOnly(libs.junit.platform.launcher)
}
