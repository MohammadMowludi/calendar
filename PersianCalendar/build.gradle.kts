import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

// https://stackoverflow.com/a/52441962
fun String.runCommand(
    workingDir: File = File("."),
    timeoutAmount: Long = 60,
    timeoutUnit: TimeUnit = TimeUnit.SECONDS
): String? = try {
    ProcessBuilder("\\s".toRegex().split(this))
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start().apply { waitFor(timeoutAmount, timeoutUnit) }
        .inputStream.bufferedReader().readText()
} catch (e: java.io.IOException) {
    e.printStackTrace()
    null
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    buildFeatures {
        viewBinding = true
    }

    val gitVersion = listOf(
        "git rev-parse --abbrev-ref HEAD",
        "git rev-list HEAD --count",
        "git rev-parse --short HEAD"
    ).joinToString("-") { it.runCommand()?.trim() ?: "" } +
            (if (("git status -s".runCommand() ?: "").isBlank()) "" else "-dirty")

    defaultConfig {
	// آی دی برنامه خود را اینجا قرار دهید . 
         applicationId = "com.siga.calendar"
        minSdkVersion(17)
        targetSdkVersion(30)
        versionCode = 2
        versionName = "1.1.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
		// زبان آلمانی به برنامه اضافه شده است احتمالا 100 درصد آلمانی نباشد در آینده ترجمه المانی دقیق تر خواهد شد 
         resConfigs("en", "fa", "ckb", "ar", "ur", "ps", "glk", "azb", "ja" ,"de")
        setProperty("archivesBaseName", "comsigacalendar-$versionName-$gitVersion")
        multiDexEnabled = false
    }

    signingConfigs {
   // کلید خودتان را اینجا قرار دهید
    }

    buildTypes {

        getByName("debug") {
            versionNameSuffix = "-${defaultConfig.versionName}-$gitVersion"
            applicationIdSuffix = ".debug"
        }

        getByName("release") {
		// تنظیمات رلیز خود را اینجا قرار دهید.
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
	// تنظیمات آی دها و سایر محتویات مختلف کدهای خود را اینجا قرار دهید 
	    flavorDimensions  "com_siga_calendar"
    productFlavors {
        create("com_siga_calendar") {
     dimension = "com_siga_calendar"
        }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    implementation("com.github.persian-calendar:equinox:1.0.0")
    implementation("com.github.persian-calendar:calendar:1.0.1")
    implementation("com.github.persian-calendar:praytimes:1.0.0")

    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.preference:preference-ktx:1.1.1")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("com.google.android:flexbox:2.0.1")

    implementation("com.google.openlocationcode:openlocationcode:1.0.4")

    val navVersion = "2.3.0"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.fragment:fragment-ktx:1.3.0")
    implementation("androidx.activity:activity-ktx:1.2.0")

    implementation("androidx.browser:browser:1.3.0")

    implementation("androidx.work:work-runtime-ktx:2.5.0")

    // debugImplementation("com.squareup.leakcanary:leakcanary-android:2.0-alpha-2")
    // debugImplementation("com.github.pedrovgs:lynx:1.1.0")

    testImplementation("junit:junit:4.13")

    androidTestImplementation("androidx.test:runner:1.3.0")
    androidTestImplementation("androidx.test:rules:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}
