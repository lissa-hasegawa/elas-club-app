plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.elasclub"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.elasclub"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.room.common.jvm)
    implementation ("androidx.room:room-runtime:2.5.0")
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.navigation.runtime.android)
    annotationProcessor ("androidx.room:room-compiler:2.5.0")
    implementation("androidx.camera:camera-camera2:1.3.3")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.libraries.places:places:3.4.0")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation(libs.room.runtime.android)
    implementation(libs.room.runtime)
    implementation ("net.zetetic:android-database-sqlcipher:4.5.0")
    implementation ("org.bouncycastle:bcprov-jdk15on:1.70")
    implementation ("org.mindrot:jbcrypt:0.4")
    implementation ("org.springframework.security:spring-security-crypto:5.8.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}