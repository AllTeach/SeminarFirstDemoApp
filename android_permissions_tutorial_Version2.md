# Android Permissions: Theory and Practice

---

## 1. **What Are Permissions?**

Permissions in Android are a security mechanism that protects user privacy and device integrity.  
They limit what your app can access—such as the camera, SMS, contacts, or internet—by requiring explicit approval.

- **Why?** To ensure apps don’t access sensitive data or features without the user's knowledge.
- **How?** By declaring permissions in the app’s `AndroidManifest.xml` and, for certain sensitive actions, requesting them at runtime.

---

## 2. **Permission Categories**

Android permissions are divided into several categories based on their potential risk to the user:

| Category           | Runtime Request? | Example Permissions                | How Granted                |
|--------------------|------------------|------------------------------------|----------------------------|
| **Normal**         | No               | `INTERNET`, `ACCESS_NETWORK_STATE` | Manifest only (auto-granted)|
| **Dangerous**      | Yes              | `CAMERA`, `READ_CONTACTS`, `ACCESS_FINE_LOCATION`, `SEND_SMS` | Manifest + runtime request |
| **Special**        | Manual (Settings)| `SYSTEM_ALERT_WINDOW`, `WRITE_SETTINGS` | User must enable in system settings |
| **Signature**      | N/A              | Custom enterprise permissions      | Only if app signatures match|

---

## 3. **How To Declare Permissions**

### **A. Normal Permissions**
- **No runtime request needed.**
- Declare in the manifest; Android grants automatically.

**Example:**  
Allowing your app to access the internet.
```xml
<uses-permission android:name="android.permission.INTERNET"/>
```
**When used:**  
```java
// No need to check or request at runtime
URL url = new URL("https://www.example.com");
```

---

### **B. Dangerous Permissions**
- **Must be declared in the manifest AND requested at runtime.**
- Examples: Camera, location, contacts, SMS, microphone.

#### **Step 1: Declare in Manifest**
```xml
<uses-permission android:name="android.permission.CAMERA"/>
```

#### **Step 2: Request at Runtime**
```java
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.widget.Toast;

// Inside your Activity or Fragment:
private ActivityResultLauncher<String> cameraPermissionLauncher;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    cameraPermissionLauncher = registerForActivityResult(
        new ActivityResultContracts.RequestPermission(),
        isGranted -> {
            if (isGranted) {
                // Permission granted, use the camera
                Toast.makeText(this, "Camera permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Camera permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    );

    findViewById(R.id.requestCameraButton).setOnClickListener(v -> {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // Already granted
            Toast.makeText(this, "Camera permission already granted!", Toast.LENGTH_SHORT).show();
        } else {
            // Request permission
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    });
}
```
**Layout Example:**
```xml
<Button
    android:id="@+id/requestCameraButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Request Camera Permission" />
```

---

## 4. **More Examples**

### **A. Normal Permission Example: Checking Network State**
- Only declared in the manifest:
```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```
- Usage (no runtime request needed):
```java
ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
```

---

### **B. Dangerous Permission Example: Sending an SMS**
- Manifest:
```xml
<uses-permission android:name="android.permission.SEND_SMS"/>
```
- Runtime request (similar to camera):
```java
smsPermissionLauncher = registerForActivityResult(
    new ActivityResultContracts.RequestPermission(),
    isGranted -> {
        if (isGranted) {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage("1234567890", null, "Hello!", null, null);
        } else {
            Toast.makeText(this, "SMS permission denied.", Toast.LENGTH_SHORT).show();
        }
    }
);
```

---

## 5. **Special Permissions Example**

Some permissions (like drawing over other apps) are handled through system settings, not the normal permission dialog.

**Example:**  
```xml
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
```
- You must prompt the user to enable it in device settings (cannot request at runtime via dialog).

---

## 6. **Summary Table**

| Permission                  | Category   | Manifest Only | Runtime Request Needed |
|-----------------------------|------------|---------------|-----------------------|
| INTERNET                    | Normal     | Yes           | No                    |
| ACCESS_NETWORK_STATE        | Normal     | Yes           | No                    |
| CAMERA                      | Dangerous  | Yes           | Yes                   |
| READ_CONTACTS               | Dangerous  | Yes           | Yes                   |
| SEND_SMS                    | Dangerous  | Yes           | Yes                   |
| SYSTEM_ALERT_WINDOW         | Special    | Yes           | User enables in settings|

---

## 7. **Best Practices**

- **Request permissions only when needed.**
- **Provide clear explanations** to the user why permissions are needed.
- **Handle denial gracefully:** Disable features or show rationale.
- **Don’t request unnecessary permissions!**

---

## 8. **References**

- [Android Developer Guide: Permissions Overview](https://developer.android.com/guide/topics/permissions/overview)
- [List of Android Permissions](https://developer.android.com/reference/android/Manifest.permission)
- [Requesting Permissions at Runtime](https://developer.android.com/training/permissions/requesting)

---