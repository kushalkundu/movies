import com.android.build.api.dsl.Packaging

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.movies"
    compileSdk = 34

    testOptions{
        packaging{
            jniLibs {
                useLegacyPackaging = true
            }
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    packaging {
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/LICENSE-notice.md")
    }


    defaultConfig {
        applicationId = "com.example.movies"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }

        release {
            isCrunchPngs= false
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
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation("androidx.test:core-ktx:1.5.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Retrofit Dependency
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    // Glide Dependency
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Room Dependency
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-paging:$roomVersion")

    // Dagger Hilt Dependency
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")

    // Coroutines Dependency
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // Nav graph Dependency
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")



    val lifecycle_version = "2.7.0"
    // ViewModel Dependency
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // LiveData Dependency
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    // Lifecycles only Dependency (without ViewModel or LiveData)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")


    implementation("androidx.collection:collection-ktx:1.3.0")

    // Timber Logger Dependency
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Json Include Dependency
    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.11.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.11.1")

    // Swipe Refresh Layout Dependency
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    val mockkVersion = "1.13.9"

    testImplementation("io.mockk:mockk-android:${mockkVersion}")
    testImplementation("io.mockk:mockk-agent:${mockkVersion}")
    androidTestImplementation("io.mockk:mockk-android:${mockkVersion}")
    androidTestImplementation("io.mockk:mockk-agent:${mockkVersion}")
    testImplementation ("app.cash.turbine:turbine:1.0.0")
    testImplementation ("io.mockk:mockk:1.13.9")


    androidTestImplementation ("androidx.arch.core:core-testing:2.2.0")
    testImplementation ("com.google.truth:truth:1.1.3")

    // Multidex Dependency
    implementation ("androidx.multidex:multidex:2.0.1")

}

kapt {
    correctErrorTypes = true

}

hilt {
    enableAggregatingTask = false
}


