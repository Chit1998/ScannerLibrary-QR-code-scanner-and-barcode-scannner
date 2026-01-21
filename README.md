# 📷 ScannerLibrary

An easy-to-use Android library for scanning QR codes and barcodes using **CameraX** and **MLKit**.

Overview
ScannerLibrary is a powerful Kotlin library designed for Android developers who want to add QR code and barcode scanning functionality to their apps effortlessly. The library is lightweight, fast, and fully customizable, making it ideal for both small and large applications.

Features

✅ Scan QR codes and barcodes quickly

✅ Easy integration into any Android project

✅ Supports custom camera settings

✅ Optional flash control for low-light environments

✅ Lightweight and fast scanning with minimal battery usage

Why Choose ScannerLibrary?

Simple Setup: Just add the dependency and start scanning.

Customizable: Control camera, flash, and scanning behavior.

Optimized for Android: Works on most devices with smooth performance.

Open Source: Free to use and modify for your projects.

---

## 🚀 Installation

Add JitPack (or MavenCentral if you publish there) in your root `settings.gradle`:

```gradle
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```
---

## Then add dependency:
```gradle
dependencies {
    implementation("com.github.Chit1998:ScannerLibrary-QR-code-scanner-and-barcode-scannner:1.0.1")
}
```
## 1. Declare permission in your AndroidManifest.xml
```gradle
<uses-permission android:name="android.permission.CAMERA" />
```

## 2. Start scanner from your Activity
```gradle
Scanner.start(
    this,
    cameraTime = 30_000,          // auto-close after 30 sec
    visibilityFlash = true,       // enable flash toggle
    cameraFacing = Scanner.CameraFacing.BACK
)
```

## 3. Handle result
```gradle
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == Scanner.REQUEST_CODE && data != null) {
        val scannedText = data.getStringExtra(Scanner.EXTRA_RESULT)
        Toast.makeText(this, "Scanned: $scannedText", Toast.LENGTH_SHORT).show()
    }
}
```
## 3. Add activity in your manifest
```gradle
<activity android:name="com.scannerlibrary.ScannerActivity"
    android:exported="false"/>
```

