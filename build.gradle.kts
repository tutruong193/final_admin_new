// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}
buildscript {
    repositories {
        google()  // Đảm bảo rằng bạn có kho Google
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.15")  // Đảm bảo rằng bạn đang sử dụng phiên bản mới nhất của Google Services plugin
    }
}
