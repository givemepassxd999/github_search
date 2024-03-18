plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.github_search_api_demo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.github_search_api_demo"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }

}



dependencies {
    ext {
        set("paging_version", "3.3.0-alpha04")
        set("kotlin_version", "3.3.0-alpha04")
        set("retrofit_version", "2.9.0")
        set("retrofit_log_version", "4.9.3")
        set("lifecycle_version", "2.7.0")
        set("refresh_version", "1.1.0")
        set("koin_version", "3.1.6")
        set("timber_version", "5.0.1")
    }

    // Timber
    implementation("com.jakewharton.timber:timber:${ext.get("timber_version")}")

    // Koin Core features
    implementation("io.insert-koin:koin-core:${ext.get("koin_version")}")
    // Koin main features for Android
    implementation("io.insert-koin:koin-android:${ext.get("koin_version")}")
    // Java Compatibility
    implementation("io.insert-koin:koin-android-compat:${ext.get("koin_version")}")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:${ext.get("retrofit_version")}")
    implementation("com.squareup.retrofit2:converter-gson:${ext.get("retrofit_version")}")
    implementation("com.squareup.retrofit2:retrofit-mock:${ext.get("retrofit_version")}")
    implementation("com.squareup.okhttp3:logging-interceptor:${ext.get("retrofit_log_version")}")

    // lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${ext.get("lifecycle_version")}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${ext.get("lifecycle_version")}")

    // paging
    implementation("androidx.paging:paging-runtime-ktx:${ext.get("paging_version")}")

    // swiperefreshlayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:${ext.get("refresh_version")}")

    // architecture components
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}