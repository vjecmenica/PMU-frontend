plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.kviz"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.kviz"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")      //

    // Firebase Authentication and Realtime Database
    implementation("com.google.firebase:firebase-auth:22.1.1")
    implementation("com.google.firebase:firebase-database:20.3.0")

    // Retrofit for network requests
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation("androidx.media3:media3-common:1.4.1")
    implementation("androidx.compose.ui:ui-text-android:1.6.8")
//    implementation("androidx.compose.material3:material3-android:1.2.1")

    // Unit testing
    testImplementation("junit:junit:4.13.2")

    // AndroidX testing
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Debugging
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.6.0-alpha03")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0-alpha03")

    //Data store
    implementation ("androidx.datastore:datastore-preferences:1.1.1")

    //splash screen
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    kapt ("com.github.bumptech.glide:compiler:4.15.1")
    implementation("io.coil-kt:coil-compose:2.2.2")

//    implementation("androidx.compose.material3:material3:<latest_version>")

}

//dependencies {
//    implementation("androidx.core:core-ktx:1.9.0")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.1") // Updated to latest version
//    implementation("androidx.activity:activity-compose:1.8.2")
//
//    // Jetpack Compose dependencies with BOM
//    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
//    implementation("androidx.compose.ui:ui")
//    implementation("androidx.compose.ui:ui-graphics")
//    implementation("androidx.compose.ui:ui-tooling-preview")
//    implementation("androidx.compose.material3:material3")
//
//    // Firebase Authentication and Realtime Database
//    implementation("com.google.firebase:firebase-auth:22.1.1")
//    implementation("com.google.firebase:firebase-database:20.3.0")
//
//    // Retrofit for network requests
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//
//    // Generative AI Client
//    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
//
//    // Android Media
//    implementation("androidx.media3:media3-common:1.4.1")
//
//    // Unit testing
//    testImplementation("junit:junit:4.13.2")
//
//    // AndroidX testing
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
//
//    // Debugging
//    debugImplementation("androidx.compose.ui:ui-tooling")
//    debugImplementation("androidx.compose.ui:ui-test-manifest")
//
//    // Navigation
//    implementation("androidx.navigation:navigation-compose:2.6.0-alpha03")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0-alpha03")
//
//    // Data store
//    implementation("androidx.datastore:datastore-preferences:1.1.1")
//
//    // Image loading libraries
//    implementation("com.github.bumptech.glide:glide:4.15.1")
//    kapt("com.github.bumptech.glide:compiler:4.15.1")
//    implementation("io.coil-kt:coil-compose:2.2.2")
//
//    // Splash screen
//    implementation("androidx.core:core-splashscreen:1.0.0")
//}
