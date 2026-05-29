# Pemetaan Materi PBO ke Sistem POS Buku

Dokumen ini merangkum bagaimana materi pertemuan 1-13 dari file Word diterapkan pada sistem Java-only.

| Materi | Implementasi di Sistem |
| --- | --- |
| Object, class, state, behavior | Class `Buku`, `User`, `Admin`, `Pelanggan`, `Transaksi`, dan `Pembayaran`. |
| Struktur Java dan `main()` | Entry point berada di `com.bookpos.App`. |
| Variabel dan tipe data | `String`, `int`, `BigDecimal`, `LocalDateTime`, `boolean`, dan enum. |
| Operator dan ekspresi | Perhitungan subtotal, total, kembalian, dan validasi stok. |
| Percabangan | Validasi login, validasi pembayaran, stok cukup/tidak cukup. |
| `switch` | Menu utama, menu admin, dan menu pelanggan pada `ConsoleMenu`. |
| Loop | Menu berjalan berulang, menampilkan daftar buku, menampilkan detail transaksi. |
| Array satu dimensi | Method `Transaksi.totalItemArrayDemo()` memakai `int[]` untuk menghitung total item. |
| Method | Service dan model dipecah menjadi method kecil seperti `hitungTotal()`, `checkout()`, `tambah()`. |
| Class dan object | Data dari penyimpanan Java dipetakan menjadi object model. |
| Constructor | Semua model memiliki constructor untuk inisialisasi object. |
| Constructor overloading | Class `Buku` punya constructor kosong, constructor data baru, dan constructor data lengkap. |
| Keyword `this` | Digunakan pada constructor untuk membedakan atribut dan parameter. |
| Encapsulation | Atribut model dibuat `private`, akses melalui getter dan method. |
| Inheritance | `Admin` dan `Pelanggan` mewarisi class abstrak `User`. |
| Polymorphism | `User.menuTitle()` dioverride oleh `Admin` dan `Pelanggan`. |
| Static | `Buku.totalObjectBuku` menunjukkan data milik class, bukan object tertentu. |
| Inner class | `Transaksi.ItemPembelian` sebagai class di dalam `Transaksi`. |
| Java collection sebagai database | `InMemoryDatabase` menyimpan data awal pada `ArrayList`. |
| UML dan rancangan sistem | Struktur class mengikuti laporan POS: Admin, Pelanggan, Buku, Transaksi, DetailTransaksi, Pembayaran. |
