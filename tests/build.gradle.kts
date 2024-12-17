plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.kqm.mydiaryapp"
    compileSdk = 35

    defaultConfig {
        minSdk = 27

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    testImplementation(project(":feature:notification"))
    testImplementation(project(":feature:calendar:presentation"))
    testImplementation(project(":feature:calendar:domain"))
    testImplementation(project(":feature:calendar:usecases"))
    testImplementation(project(":feature:calendar:framework"))
    testImplementation(project(":feature:calendar:data"))
    androidTestImplementation(project(":feature:calendar:framework"))
    androidTestImplementation(project(":feature:calendar:presentation"))
    androidTestImplementation(project(":feature:calendar:domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.paging.common.ktx)
    implementation(libs.pagination.runtime.ktx)


    testImplementation(libs.junit)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.pagination.common)
    testImplementation(libs.androidx.paging.common.ktx)
    testImplementation(libs.pagination.testing)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.material)
    androidTestImplementation(libs.androidx.material3)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.pagination.common)
    androidTestImplementation(libs.androidx.paging.common.ktx)
    androidTestImplementation(libs.androidx.paging.compose.android)
    androidTestImplementation(libs.pagination.testing)
    androidTestImplementation(libs.androidx.room.ktx)
    androidTestImplementation(libs.hilt.android.testing)
}