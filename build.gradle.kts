plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

group = "com.antoinecampbell.operaton"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.spring.boot)
    implementation(platform(libs.operaton.bom))
    implementation(libs.bundles.operaton)
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.jackson)
    implementation(libs.bundles.database)
    implementation(libs.bundles.metrics)

    // Testing
    developmentOnly(libs.spring.boot.devtools)
    testImplementation(libs.bundles.testing)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
