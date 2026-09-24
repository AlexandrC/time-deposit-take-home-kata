plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)   // opens @Service/@Transactional classes for proxies
}
dependencies {
    implementation(project(":core"))
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.spring.context)
    implementation(libs.spring.tx)


}
