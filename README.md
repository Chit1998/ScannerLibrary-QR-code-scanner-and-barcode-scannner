# 📷 ScannerLibrary

An easy-to-use Android library for scanning QR codes and barcodes using **CameraX** and **MLKit**.

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
    implementation 'com.github.chit1998:scannerlibrary:1.0.0'
}
```
## 1. Declare permission in your AndroidManifest.xml
```gradle
<uses-permission android:name="android.permission.CAMERA" />
```

## 3. Start scanner from your Activity
```gradle
Scanner.start(
    this,
    cameraTime = 30_000,          // auto-close after 30 sec
    visibilityFlash = true,       // enable flash toggle
    cameraFacing = Scanner.CameraFacing.BACK
)
```

## 5. Handle result
```gradle
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == Scanner.REQUEST_CODE && data != null) {
        val scannedText = data.getStringExtra(Scanner.EXTRA_RESULT)
        Toast.makeText(this, "Scanned: $scannedText", Toast.LENGTH_SHORT).show()
    }
}
```
