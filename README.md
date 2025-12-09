# 📰 NewsApp – Jetpack Compose News Application  

Aplikasi berita Android modern berbasis **Jetpack Compose**, menggunakan **NewsAPI.org** sebagai sumber berita, **Supabase** untuk autentikasi pengguna, serta **Hilt** untuk dependency injection.  
Dibangun dengan arsitektur **MVVM + Repository Pattern**, aplikasi ini memiliki UI modern, reaktif, dan mudah untuk dikembangkan.

---

## ✨ Fitur Utama

### 🔐 Autentikasi Pengguna
- Login & Register menggunakan **Supabase Auth**
- Halaman **User Profile** (nama & email)

### 📰 Berita
- Menampilkan daftar berita terbaru dari **NewsAPI**
- Load gambar berita menggunakan **Coil**
- Halaman **Detail News** berisi gambar, judul, dan deskripsi singkat
- Tombol **“Read More”** untuk membuka artikel asli dalam **WebView**

### 🎨 UI/UX
- Full **Jetpack Compose**
- Material Design 3
- Clean, modern, dan responsif
- Navigasi menggunakan **Navigation Compose**

---

## 🛠️ Teknologi yang Digunakan

| Teknologi | Penggunaan |
|----------|------------|
| **Kotlin** | Bahasa utama |
| **Jetpack Compose** | UI declarative |
| **MVVM Architecture** | Manajemen state & data |
| **Hilt** | Dependency Injection |
| **Retrofit** | Konsumsi API NewsAPI |
| **Supabase Auth** | Login, register, session |
| **Supabase Database** | Menyimpan profil user |
| **StateFlow / Coroutine** | Reactive state |
| **Coil** | Load image |
| **WebView** | Read more |

---

## 📐 Arsitektur

Struktur arsitektur proyek mengikuti pola: Interface → Repository → ViewModel → UI (Jetpack Compose)

![NewsApp](https://github.com/ardhaniahlan/jetpack-compose-supabase-news-app/blob/main/newsapp.gif)

