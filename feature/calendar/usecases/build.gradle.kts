import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.ksp)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    implementation(project(":feature:calendar:domain"))
    implementation(project(":feature:calendar:data"))

//    implementation(libs.pagination.runtime)
//    implementation(libs.pagination.compose)

    implementation(libs.hilt.core)
    implementation(libs.androidx.paging.common.ktx)
    ksp(libs.hilt.compiler)
}