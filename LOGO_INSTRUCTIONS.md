# Instruksi Logo untuk zacode

## Lokasi File Logo

Letakkan file logo dengan nama **`logo.png`** di folder berikut:

```
kotlin/app/src/main/res/drawable/logo.png
```

**Path lengkap:**
```
C:\Users\afriz\OneDrive\Desktop\all-template-extension\kotlin\app\src\main\res\drawable\logo.png
```

## Spesifikasi Logo

- **Nama file:** `logo.png`
- **Ukuran maksimal:** 32 KB (atau lebih kecil)
- **Format:** PNG (recommended) atau WebP
- **Dimensi recommended:**
  - Minimal: 512x512 px (untuk kualitas tinggi)
  - Optimal: 1024x1024 px
  - Maksimal: tidak ada batasan pixel, tetapi ukuran file harus ≤ 32 KB

## Cara Menggunakan Logo

Setelah menambahkan `logo.png` ke folder `res/drawable/`, logo dapat digunakan di aplikasi dengan referensi:

```kotlin
@drawable/logo
```

Atau di XML:
```xml
android:src="@drawable/logo"
```

## Tips Optimasi Logo untuk 32 KB

1. **Kompres gambar:** Gunakan tool seperti TinyPNG atau ImageOptim
2. **Kurangi resolusi:** Jika logo terlalu besar, kurangi dimensi
3. **Hapus metadata:** Pastikan tidak ada EXIF data yang tidak perlu
4. **Gunakan WebP:** Format WebP lebih kecil dari PNG (opsional)

## Catatan

Logo ini dapat digunakan untuk:
- Icon aplikasi (perlu di-convert ke berbagai density untuk mipmap folders)
- Logo di splash screen
- Logo di header aplikasi
- Logo di berbagai screen UI

Jika ingin logo sebagai **icon launcher**, letakkan di:
- `res/mipmap-hdpi/logo.png` (72x72 px)
- `res/mipmap-mdpi/logo.png` (48x48 px)
- `res/mipmap-xhdpi/logo.png` (96x96 px)
- `res/mipmap-xxhdpi/logo.png` (144x144 px)
- `res/mipmap-xxxhdpi/logo.png` (192x192 px)

