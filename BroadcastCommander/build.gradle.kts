import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    id("com.vanniktech.maven.publish") version "0.30.0"
}

val aarVersion: String? = gradleLocalProperties(rootDir, providers).getProperty("VERSION_NAME")

android {
    namespace = "io.github.airdaydreamers.broadcastcommander"
    compileSdk = 34

    defaultConfig {
        minSdk = 29

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

//maven
//publishReleasePublicationToMavenCentralRepository
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "io.github.airdaydreamers.broadcastcommander"
            artifactId = "broadcastcommander"

            version = aarVersion ?: "0.0.1-alpha"

            pom {
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }

                developers {
                    name = "broadcastcommander"
                    description =
                        "A lightweight Android library for handling broadcast commands with ease through XML configuration."
                    url = "https://github.com/vladislav-smirnov/BroadcastCommander"

                    developer {
                        id = "vladislavsmirnov"
                        name = "Vladislav Smirnov"
                        email = "sivdead@gmail.com"
                    }
                }

                scm {
                    connection = "scm:git:git://github.com/vladislav-smirnov/BroadcastCommander.git"
                    developerConnection =
                        "scm:git:ssh://github.com:vladislav-smirnov/BroadcastCommander.git"
                    url = "https://github.com/vladislav-smirnov/BroadcastCommander"
                }
            }

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

dependencies {
    implementation(libs.reflections)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}