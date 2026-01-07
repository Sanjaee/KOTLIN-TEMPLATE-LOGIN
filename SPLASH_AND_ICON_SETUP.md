# Setup Splash Screen dan Launcher Icon

## ✅ Perubahan yang Telah Dilakukan

### 1. **Launcher Icon (Icon APK)**
- ✅ Background: Putih (`ic_launcher_background.xml`)
- ✅ Foreground: Logo zacode (`logo_foreground.xml`)
- ✅ Adaptive Icon: Sudah dikonfigurasi untuk Android 8+ (API 26+)
- ✅ Round Icon: Sudah dikonfigurasi

**File yang diubah:**
- `res/drawable/ic_launcher_background.xml` - Background putih
- `res/drawable/logo_foreground.xml` - Logo sebagai foreground
- `res/mipmap-anydpi-v26/ic_launcher.xml` - Adaptive icon
- `res/mipmap-anydpi-v26/ic_launcher_round.xml` - Round adaptive icon

### 2. **Splash Screen (Loading Screen)**
- ✅ Splash screen dengan logo zacode
- ✅ Background putih
- ✅ Durasi: 2 detik
- ✅ Otomatis pindah ke Login atau Home screen

**File yang diubah:**
- `res/drawable/splash_background.xml` - Background splash dengan logo
- `res/values/themes.xml` - Theme.Splash untuk splash screen
- `AndroidManifest.xml` - Activity menggunakan Theme.Splash
- `MainActivity.kt` - Implementasi splash screen dengan Compose

## 📱 Cara Kerja

### Launcher Icon
1. Icon aplikasi di home screen Android akan menampilkan logo zacode
2. Background putih dengan logo di tengah
3. Otomatis beradaptasi dengan berbagai bentuk icon (square, round, dll)

### Splash Screen
1. Saat aplikasi dibuka, splash screen muncul dengan logo zacode
2. Logo ditampilkan di tengah dengan background putih
3. Setelah 2 detik, splash screen hilang dan aplikasi masuk ke Login atau Home screen

## 🔧 Catatan Penting

### Untuk Launcher Icon yang Lebih Baik:
Jika logo tidak terlihat sempurna sebagai adaptive icon, Anda bisa:
1. Buat logo dengan padding yang cukup (safe zone)
2. Pastikan logo berada di area 66% tengah (Android adaptive icon safe zone)
3. Atau buat versi logo khusus untuk icon (lebih sederhana, tanpa detail kecil)

### Untuk Splash Screen:
- Durasi splash screen bisa diubah di `MainActivity.kt` (baris 69: `delay(2000)`)
- Warna background bisa diubah di `splash_background.xml`
- Ukuran logo bisa diubah di `MainActivity.kt` (baris 62: `Modifier.size(150.dp)`)

## 🚀 Testing

Setelah build APK:
1. Install APK di device
2. Cek icon aplikasi di home screen - harus menampilkan logo zacode
3. Buka aplikasi - harus muncul splash screen dengan logo
4. Setelah 2 detik, aplikasi masuk ke Login atau Home screen

## 📝 File yang Terlibat

```
kotlin/app/src/main/
├── res/
│   ├── drawable/
│   │   ├── ic_launcher_background.xml (Background putih)
│   │   ├── logo_foreground.xml (Logo untuk icon)
│   │   └── splash_background.xml (Background splash)
│   ├── mipmap-anydpi-v26/
│   │   ├── ic_launcher.xml (Adaptive icon)
│   │   └── ic_launcher_round.xml (Round adaptive icon)
│   └── values/
│       └── themes.xml (Theme.Splash)
├── AndroidManifest.xml (Theme.Splash untuk activity)
└── java/.../MainActivity.kt (Splash screen logic)
```

## ✨ Hasil

- ✅ Icon APK: Logo zacode dengan background putih
- ✅ Splash Screen: Logo zacode saat aplikasi dibuka
- ✅ Tidak lagi menggunakan icon default Android

