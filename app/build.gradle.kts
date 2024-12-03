plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.cs_5520_final"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cs_5520_final"
        minSdk = 24
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
            buildConfigField(
                "String",
                "OPENAI_API_KEY",
                "\"${System.getenv("OPENAI_API_KEY_IMG") ?: "REPLACE_WITH_API_KEY"}\""
            )
        }
        debug {
            buildConfigField(
                "String",
                "OPENAI_API_KEY",
                "\"${System.getenv("OPENAI_API_KEY_IMG") ?: "REPLACE_WITH_API_KEY"}\""
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.recyclerview)
    implementation(libs.room.common)
    implementation(libs.room.runtime)
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("org.json:json:20210307")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    annotationProcessor(libs.room.compiler)
}
