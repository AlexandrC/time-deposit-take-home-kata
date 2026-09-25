plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.spring) // also applies allopen, needed for the allOpen block below
	alias(libs.plugins.kotlin.jpa)    // no-arg constructors for @Entity
}

allOpen {
	// Hibernate needs non-final entities for lazy proxies
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

dependencies {
	implementation(project(":core"))
	implementation(platform(libs.spring.boot.bom))
	implementation(libs.spring.boot.starter.data.jpa)
	implementation(libs.kotlin.reflect) // Spring Data inspects Kotlin entity constructors via kotlin-reflect

	testImplementation(libs.spring.boot.starter.data.jpa.test)
	testImplementation(libs.spring.boot.starter.flyway)
	testImplementation(libs.spring.boot.testcontainers)
	testImplementation(libs.testcontainers.postgres)
	testRuntimeOnly(libs.postgresql)
	testRuntimeOnly(libs.flyway.database.postgresql)
	testRuntimeOnly(libs.junit.platform.launcher)
}
