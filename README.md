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
Then add dependency:

gradle
Copy code
dependencies {
    implementation 'com.github.yourusername:scannerlibrary:1.0.0'
}
📌 Usage
1. Declare permission in your AndroidManifest.xml
xml
Copy code
<uses-permission android:name="android.permission.CAMERA" />
2. Start scanner from your Activity
kotlin
Copy code
Scanner.start(
    this,
    cameraTime = 30_000,          // auto-close after 30 sec
    visibilityFlash = true,       // enable flash toggle
    cameraFacing = Scanner.CameraFacing.BACK
)
3. Handle result
kotlin
Copy code
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == Scanner.REQUEST_CODE && data != null) {
        val scannedText = data.getStringExtra(Scanner.EXTRA_RESULT)
        Toast.makeText(this, "Scanned: $scannedText", Toast.LENGTH_SHORT).show()
    }
}
