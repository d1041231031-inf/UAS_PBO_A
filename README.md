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

## Catatan Data

Karena database memakai memori Java, perubahan data akan hilang saat program ditutup. Setiap program dijalankan ulang, data awal akan kembali dari `InMemoryDatabase.withSeedData()`.

## Catatan Akademik

Pemetaan materi PBO pertemuan 1-13 ke kode ada di `docs/pemetaan_materi_pbo.md`.
