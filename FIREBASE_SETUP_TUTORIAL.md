# Firebase Setup Tutorial untuk Google Sign In

Panduan lengkap untuk setup Firebase Authentication dengan Google Sign In di aplikasi Android Kotlin.

## 📋 Prasyarat

1. Akun Google (Gmail)
2. Android Studio dengan Android SDK terbaru
3. Aplikasi Android yang sudah dikonfigurasi

## 🔥 Langkah 1: Buat Project di Firebase Console

1. Buka [Firebase Console](https://console.firebase.google.com/)
2. Klik **"Add project"** atau **"Create a project"**
3. Masukkan nama project (contoh: "zacode-app")
4. Ikuti wizard untuk menyelesaikan pembuatan project
5. Setelah project dibuat, klik **"Continue"**

## 📱 Langkah 2: Tambahkan Android App ke Firebase

1. Di halaman project overview, klik ikon **Android** (ikon Android hijau)
2. Masukkan package name aplikasi Anda:
   ```
   com.example.myapplication
   ```
   (Sesuaikan dengan package name di `build.gradle.kts` Anda)
3. Isi **App nickname** (opsional): "zacode"
4. Masukkan **Debug signing certificate SHA-1** (opsional untuk development)
   - Untuk mendapatkan SHA-1, jalankan di terminal:
     ```bash
     cd android
     ./gradlew signingReport
     ```
     - Copy SHA-1 fingerprint dari output
5. Klik **"Register app"**

## 📥 Langkah 3: Download `google-services.json`

1. Setelah register app, Firebase akan menampilkan link download `google-services.json`
2. Download file tersebut
3. Copy file `google-services.json` ke folder:
   ```
   kotlin/app/
   ```
   Struktur folder harus seperti ini:
   ```
   kotlin/
   └── app/
       └── google-services.json  ← Letakkan file di sini
   ```

## 🔐 Langkah 4: Enable Google Authentication

1. Di Firebase Console, buka **Authentication** di menu kiri
2. Klik tab **"Sign-in method"**
3. Klik pada **"Google"**
4. Toggle **"Enable"** untuk mengaktifkan Google Sign In
5. Pilih **Support email** (biasanya email project Anda)
6. Klik **"Save"**

## 🔑 Langkah 5: Dapatkan Web Client ID

1. Di halaman **Authentication** > **Sign-in method** > **Google**
2. Klik **"Web SDK configuration"**
3. Copy **Web client ID** (berbentuk: `xxxxx.apps.googleusercontent.com`)
4. Buka file: `kotlin/app/src/main/res/values/strings.xml`
5. Tambahkan string resource:
   ```xml
   <string name="google_web_client_id">YOUR_WEB_CLIENT_ID_HERE</string>
   ```
   Ganti `YOUR_WEB_CLIENT_ID_HERE` dengan Web client ID yang sudah dicopy

## 🛠️ Langkah 6: Update GoogleSignInHelper

File `GoogleSignInHelper.kt` sudah dibuat. Pastikan untuk mengambil WEB_CLIENT_ID dari string resources:

1. Buka `kotlin/app/src/main/java/com/example/myapplication/util/GoogleSignInHelper.kt`
2. Update kode untuk membaca dari resources:
   ```kotlin
   fun getGoogleSignInClient(context: Context): GoogleSignInClient {
       val webClientId = context.getString(R.string.google_web_client_id)
       val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
           .requestIdToken(webClientId)
           .requestEmail()
           .build()
       
       return GoogleSignIn.getClient(context, gso)
   }
   ```

## ✅ Langkah 7: Verifikasi Konfigurasi

### Checklist:
- [ ] File `google-services.json` sudah ada di `kotlin/app/`
- [ ] Plugin `google-services` sudah ditambahkan di `build.gradle.kts`
- [ ] Dependencies Firebase sudah ditambahkan
- [ ] Google Authentication sudah diaktifkan di Firebase Console
- [ ] Web Client ID sudah ditambahkan ke `strings.xml`
- [ ] `GoogleSignInHelper.kt` sudah menggunakan Web Client ID dari resources

## 🚀 Langkah 8: Build dan Test

1. Sync project dengan Gradle:
   ```
   File > Sync Project with Gradle Files
   ```
2. Build project:
   ```
   Build > Make Project
   ```
3. Run aplikasi di emulator atau device
4. Klik tombol **"Sign in with Google"** di halaman login
5. Pilih akun Google untuk login

## 📝 Catatan Penting

### SHA-1 Fingerprint untuk Production

Untuk production, Anda perlu menambahkan SHA-1 dari keystore production Anda:
1. Di Firebase Console, buka **Project Settings** > **Your apps** > **Android app**
2. Klik **"Add fingerprint"**
3. Masukkan SHA-1 dari keystore production Anda

### OAuth Consent Screen (jika diperlukan)

Jika Anda mendapatkan error tentang OAuth consent screen:
1. Buka [Google Cloud Console](https://console.cloud.google.com/)
2. Pilih project Firebase Anda
3. Buka **APIs & Services** > **OAuth consent screen**
4. Lengkapi informasi yang diperlukan
5. Tambahkan test users jika masih dalam mode testing

## 🐛 Troubleshooting

### Error: "10: Failed to get Google ID token"
- Pastikan Web Client ID sudah benar di `strings.xml`
- Pastikan `google-services.json` sudah ada dan benar

### Error: "DEVELOPER_ERROR"
- Pastikan SHA-1 fingerprint sudah ditambahkan di Firebase Console
- Pastikan package name sesuai antara Firebase dan aplikasi

### Error: "12500: Sign in failed"
- Pastikan Google Sign In sudah diaktifkan di Firebase Console
- Pastikan OAuth consent screen sudah dikonfigurasi

### Tombol Google Sign In tidak muncul
- Pastikan semua dependencies sudah di-sync
- Clean dan rebuild project:
  ```
  Build > Clean Project
  Build > Rebuild Project
  ```

## 📚 Referensi

- [Firebase Authentication Documentation](https://firebase.google.com/docs/auth)
- [Google Sign In for Android](https://developers.google.com/identity/sign-in/android)
- [Firebase Android Setup Guide](https://firebase.google.com/docs/android/setup)

## 🎉 Selesai!

Setelah semua langkah di atas selesai, aplikasi Anda sudah siap untuk menggunakan Google Sign In. Pastikan semua checklist sudah dicentang sebelum melakukan testing.
