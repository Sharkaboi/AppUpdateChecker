plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

group = "com.sharkaboi"
val versionName = "1.0-SNAPSHOT"
version = versionName

repositories {
    google()
    mavenCentral()
}

android {
    compileSdkVersion(31)
    buildToolsVersion = "30.0.3"

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(31)
        versionCode = 1
        versionName = versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
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

    sourceSets.getByName("main") {
        java.srcDir("src/main/kotlin")
    }
    sourceSets.getByName("test") {
        java.srcDir("src/test/kotlin")
    }
    sourceSets.getByName("androidTest") {
        java.srcDir("src/androidTest/kotlin")
    }
}

dependencies {
    api(kotlin("stdlib"))
    implementation("androidx.core:core-ktx:${Versions.ktxCoreVersion}")
    implementation("androidx.appcompat:appcompat:${Versions.appCompatVersion}")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVersion}")
    api("com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}")
    api("com.squareup.moshi:moshi-kotlin:${Versions.moshiVersion}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshiVersion}")
    api("com.squareup.retrofit2:converter-moshi:${Versions.moshiRetrofitVersion}")
    api("com.squareup.retrofit2:converter-simplexml:${Versions.simpleXmlVersion}")
    api("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.coroutineCallAdapterVersion}")
    testImplementation("junit:junit:${Versions.jUnitVersion}")
    testImplementation("io.mockk:mockk:${Versions.mockkVersion}")
}