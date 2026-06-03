# Sistem POS Penjualan Buku Online

Sistem POS Java untuk tugas PBO berdasarkan laporan POS buku online. Versi ini **Java-only**: data disimpan sementara di memori program memakai collection Java, tanpa SQL, tanpa MySQL, dan tanpa JDBC connector. Program tersedia dalam dua mode: console dan GUI Java Swing.

## Nama Anggota
- Arif Fadhilah (D1041231031)
- Djapianus Thebrianto (D1041231020)
- Rayhan Nuerjamman (D1041231088)

## Fitur

- Login admin dan pelanggan.
- Registrasi pelanggan.
- Admin dapat melihat, menambah, update stok, dan menghapus buku.
- Pelanggan dapat melihat buku, mencari buku, membeli buku, membayar, dan melihat riwayat transaksi.
- Versi GUI dibuat menggunakan Java Swing.
- Stok buku otomatis berkurang setelah transaksi berhasil.
- Data buku, user, dan transaksi tersimpan selama program berjalan.

## Akun Contoh

- Admin: `admin` / `admin123`
- Pelanggan: `rayhan` / `rayhan123`
- Pelanggan: `arif` / `arif123`

## Compile

Jalankan dari folder proyek:

```powershell
cd "D:\Kuliah\Semester 6\PBO\UAS_PBO_POS"
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
```

## Run GUI Swing

```powershell
java -cp out com.bookpos.AppGui
```

## Run Console

```powershell
java -cp out com.bookpos.App
```

## Cara Penggunaan

### 1. Mode Console
1. Jalankan aplikasi console dengan perintah di atas.
2. Pada menu utama, pilih:
   - `1. Login Admin`: masuk sebagai admin.
   - `2. Login Pelanggan`: masuk sebagai pelanggan.
   - `3. Registrasi Pelanggan`: daftar akun pelanggan baru.
   - `0. Keluar`: keluar dari aplikasi.
3. Setelah login admin, menu admin menyediakan:
   - `Tampilkan Buku`: lihat daftar buku yang tersedia.
   - `Tambah Buku`: tambahkan buku baru ke sistem.
   - `Update Stok Buku`: ubah jumlah stok buku.
   - `Hapus Buku`: hapus buku dari daftar.
   - `Lihat Transaksi`: tampilkan semua transaksi di sistem.
4. Setelah login pelanggan, menu pelanggan menyediakan:
   - `Lihat Buku`: lihat daftar buku yang tersedia.
   - `Cari Buku`: cari buku berdasarkan kata kunci.
   - `Beli Buku`: pilih buku berdasarkan ID dan jumlah, lalu bayar.
   - `Riwayat Transaksi`: lihat riwayat transaksi pelanggan.

### 2. Mode GUI
1. Jalankan aplikasi GUI dengan perintah `java -cp out com.bookpos.AppGui`.
2. Antarmuka GUI menyediakan form login dan menu yang serupa dengan fitur console.
3. Gunakan akun admin untuk mengelola buku dan melihat transaksi.
4. Gunakan akun pelanggan untuk melihat buku, membeli, dan melihat riwayat transaksi.

### 3. Contoh Akun
- Admin: `admin` / `admin123`
- Pelanggan: `rayhan` / `rayhan123`
- Pelanggan: `arif` / `arif123`

## Catatan Data

Karena database memakai memori Java, perubahan data akan hilang saat program ditutup. Setiap program dijalankan ulang, data awal akan kembali dari `InMemoryDatabase.withSeedData()`.

## Catatan Akademik

Pemetaan materi PBO pertemuan 1-13 ke kode ada di `docs/pemetaan_materi_pbo.md`.
