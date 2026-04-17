plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.andrea.subscriptionlist.core.common"
    compileSdk = 36

    defaultConfig {
        minSdk = 30
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

kotlin {
    jvmToolchain(11)
}
