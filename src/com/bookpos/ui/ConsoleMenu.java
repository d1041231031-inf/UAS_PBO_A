package com.bookpos.ui;

import com.bookpos.model.Admin;
import com.bookpos.model.Buku;
import com.bookpos.model.Pelanggan;
import com.bookpos.model.Transaksi;
import com.bookpos.service.AuthService;
import com.bookpos.service.BukuService;
import com.bookpos.service.TransaksiService;
import com.bookpos.service.TransaksiService.CartItem;
import com.bookpos.util.ConsoleInput;
import com.bookpos.util.Money;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleMenu {
    private final AuthService authService;
    private final BukuService bukuService;
    private final TransaksiService transaksiService;
    private final ConsoleInput input;

    public ConsoleMenu(AuthService authService, BukuService bukuService, TransaksiService transaksiService) {
        this.authService = authService;
        this.bukuService = bukuService;
        this.transaksiService = transaksiService;
        this.input = new ConsoleInput(new Scanner(System.in));
    }

    public void run() {
        System.out.println("=== SISTEM POS PENJUALAN BUKU ONLINE ===");
        boolean running = true;
        while (running) {
            try {
                System.out.println();
                System.out.println("1. Login Admin");
                System.out.println("2. Login Pelanggan");
                System.out.println("3. Registrasi Pelanggan");
                System.out.println("0. Keluar");
                int choice = input.integer("Pilih menu: ");

                switch (choice) {
                    case 1 -> loginAdmin();
                    case 2 -> loginPelanggan();
                    case 3 -> registrasi();
                    case 0 -> running = false;
                    default -> System.out.println("Menu tidak tersedia.");
                }
            } catch (IllegalArgumentException exception) {
                System.out.println("Validasi gagal: " + exception.getMessage());
            }
        }
        System.out.println("Terima kasih.");
    }

    private void loginAdmin() {
        String username = input.text("Username: ");
        String password = input.text("Password: ");
        Optional<Admin> admin = authService.loginAdmin(username, password);

        if (admin.isEmpty()) {
            System.out.println("Login admin gagal. Pastikan akun yang digunakan adalah akun admin.");
            return;
        }

        System.out.println("Login berhasil sebagai " + admin.get().getNama() + " - " + admin.get().menuTitle());
        menuAdmin(admin.get());
    }

    private void loginPelanggan() {
        String username = input.text("Username: ");
        String password = input.text("Password: ");
        Optional<Pelanggan> pelanggan = authService.loginPelanggan(username, password);

        if (pelanggan.isEmpty()) {
            System.out.println("Login pelanggan gagal. Pastikan akun yang digunakan adalah akun pelanggan.");
            return;
        }

        System.out.println("Login berhasil sebagai " + pelanggan.get().getNama() + " - " + pelanggan.get().menuTitle());
        menuPelanggan(pelanggan.get());
    }

    private void registrasi() {
        String nama = input.text("Nama: ");
        String username = input.text("Username: ");
        String password = input.text("Password: ");
        String email = input.text("Email: ");
        String alamat = input.text("Alamat: ");
        Pelanggan pelanggan = authService.registerPelanggan(nama, username, password, email, alamat);
        System.out.println("Registrasi berhasil. ID pelanggan: " + pelanggan.getIdPelanggan());
    }

    private void menuAdmin(Admin admin) {
        boolean active = true;
        while (active) {
            System.out.println();
            System.out.println("=== " + admin.menuTitle() + " ===");
            System.out.println("1. Tampilkan Buku");
            System.out.println("2. Tambah Buku");
            System.out.println("3. Update Stok Buku");
            System.out.println("4. Hapus Buku");
            System.out.println("5. Lihat Transaksi");
            System.out.println("0. Logout");
            int choice = input.integer("Pilih menu: ");

            try {
                switch (choice) {
                    case 1 -> tampilkanBuku(bukuService.tampilSemua());
                    case 2 -> tambahBuku();
                    case 3 -> updateStokBuku();
                    case 4 -> hapusBuku();
                    case 5 -> tampilkanTransaksi(transaksiService.semuaTransaksi());
                    case 0 -> active = false;
                    default -> System.out.println("Menu tidak tersedia.");
                }
            } catch (IllegalArgumentException exception) {
                System.out.println("Validasi gagal: " + exception.getMessage());
            }
        }
    }

    private void menuPelanggan(Pelanggan pelanggan) {
        boolean active = true;
        while (active) {
            System.out.println();
            System.out.println("=== " + pelanggan.menuTitle() + " ===");
            System.out.println("1. Lihat Buku");
            System.out.println("2. Cari Buku");
            System.out.println("3. Beli Buku");
            System.out.println("4. Riwayat Transaksi");
            System.out.println("0. Logout");
            int choice = input.integer("Pilih menu: ");

            try {
                switch (choice) {
                    case 1 -> tampilkanBuku(bukuService.tampilSemua());
                    case 2 -> cariBuku();
                    case 3 -> beliBuku(pelanggan);
                    case 4 -> tampilkanTransaksi(transaksiService.riwayatPelanggan(pelanggan));
                    case 0 -> active = false;
                    default -> System.out.println("Menu tidak tersedia.");
                }
            } catch (IllegalArgumentException exception) {
                System.out.println("Validasi gagal: " + exception.getMessage());
            }
        }
    }

    private void tambahBuku() {
        String judul = input.text("Judul: ");
        String penulis = input.text("Penulis: ");
        String genre = input.text("Genre: ");
        BigDecimal harga = input.decimal("Harga: ");
        int stok = input.integer("Stok: ");
        Buku buku = bukuService.tambah(judul, penulis, genre, harga, stok);
        System.out.println("Buku berhasil ditambahkan dengan ID " + buku.getIdBuku());
    }

    private void updateStokBuku() {
        int id = input.integer("ID buku yang stoknya diupdate: ");
        int stok = input.integer("Stok baru: ");
        if (bukuService.updateStok(id, stok)) {
            System.out.println("Stok buku berhasil diupdate.");
        } else {
            System.out.println("Buku tidak ditemukan.");
        }
    }

    private void hapusBuku() {
        int id = input.integer("ID buku yang dihapus: ");
        if (bukuService.hapus(id)) {
            System.out.println("Buku berhasil dihapus.");
        } else {
            System.out.println("Buku tidak ditemukan.");
        }
    }

    private void cariBuku() {
        String keyword = input.text("Kata kunci: ");
        tampilkanBuku(bukuService.cari(keyword));
    }

    private void beliBuku(Pelanggan pelanggan) {
        tampilkanBuku(bukuService.tampilSemua());
        List<CartItem> cart = new ArrayList<>();

        boolean tambahLagi = true;
        while (tambahLagi) {
            int idBuku = input.integer("ID buku: ");
            int jumlah = input.integer("Jumlah: ");
            cart.add(new CartItem(idBuku, jumlah));

            String lanjut = input.text("Tambah buku lain? (y/n): ");
            tambahLagi = lanjut.equalsIgnoreCase("y");
        }

        BigDecimal totalBayar = tampilkanRingkasanPembelian(cart);
        while (true) {
            String metode = pilihMetodePembayaran();
            BigDecimal jumlahBayar = input.decimal("Jumlah bayar: ");
            if (jumlahBayar.compareTo(totalBayar) < 0) {
                System.out.println("Pembayaran gagal. Uang kurang. Total yang harus dibayar: "
                        + Money.rupiah(totalBayar));
                continue;
            }

            Transaksi transaksi = transaksiService.checkout(pelanggan, cart, metode, jumlahBayar);
            cetakStruk(transaksi);
            return;
        }
    }

    private BigDecimal tampilkanRingkasanPembelian(List<CartItem> cart) {
        Map<Integer, Integer> jumlahPerBuku = new LinkedHashMap<>();
        for (CartItem item : cart) {
            jumlahPerBuku.merge(item.idBuku(), item.jumlah(), Integer::sum);
        }

        System.out.println();
        System.out.println("========== RINGKASAN PEMBELIAN ==========");
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Integer, Integer> entry : jumlahPerBuku.entrySet()) {
            Buku buku = bukuService.cariById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Buku ID " + entry.getKey() + " tidak ditemukan."));
            int jumlah = entry.getValue();
            if (!buku.stokCukup(jumlah)) {
                throw new IllegalArgumentException("Stok buku \"" + buku.getJudul() + "\" tidak cukup.");
            }

            BigDecimal subtotal = buku.getHarga().multiply(BigDecimal.valueOf(jumlah));
            total = total.add(subtotal);
            System.out.printf("%s x%d = %s%n", buku.getJudul(), jumlah, Money.rupiah(subtotal));
        }
        System.out.println("-----------------------------------------");
        System.out.println("Total yang harus dibayar: " + Money.rupiah(total));
        System.out.println("=========================================");
        return total;
    }

    private String pilihMetodePembayaran() {
        while (true) {
            System.out.println();
            System.out.println("Pilih metode pembayaran:");
            System.out.println("1. Transfer Bank");
            System.out.println("2. E-Wallet");
            int pilihan = input.integer("Pilihan: ");

            switch (pilihan) {
                case 1 -> {
                    return "Transfer Bank";
                }
                case 2 -> {
                    return "E-Wallet";
                }
                default -> System.out.println("Metode pembayaran tidak tersedia.");
            }
        }
    }

    private void tampilkanBuku(List<Buku> books) {
        if (books.isEmpty()) {
            System.out.println("Data buku kosong.");
            return;
        }

        System.out.println();
        System.out.printf("%-5s %-28s %-20s %-14s %12s %6s%n",
                "ID", "Judul", "Penulis", "Genre", "Harga", "Stok");
        for (Buku buku : books) {
            System.out.printf("%-5d %-28s %-20s %-14s %12s %6d%n",
                    buku.getIdBuku(),
                    pendek(buku.getJudul(), 28),
                    pendek(buku.getPenulis(), 20),
                    pendek(buku.getGenre(), 14),
                    Money.rupiah(buku.getHarga()),
                    buku.getStok());
        }
    }

    private void tampilkanTransaksi(List<Transaksi> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("Belum ada transaksi.");
            return;
        }

        for (Transaksi transaksi : transactions) {
            cetakStruk(transaksi);
        }
    }

    private void cetakStruk(Transaksi transaksi) {
        System.out.println();
        System.out.println("========== STRUK TRANSAKSI ==========");
        System.out.println("ID Transaksi : " + transaksi.getIdTransaksi());
        System.out.println("Tanggal      : " + transaksi.getTanggal());
        System.out.println("Pelanggan    : " + transaksi.getPelanggan().getNama());
        System.out.println("-------------------------------------");

        for (Transaksi.ItemPembelian item : transaksi.getItems()) {
            System.out.printf("%s x%d = %s%n",
                    item.getBuku().getJudul(),
                    item.getJumlah(),
                    Money.rupiah(item.hitungSubtotal()));
        }

        System.out.println("-------------------------------------");
        System.out.println("Total item   : " + transaksi.totalItemArrayDemo());
        System.out.println("Total        : " + Money.rupiah(transaksi.hitungTotal()));
        System.out.println("Metode       : " + transaksi.getPembayaran().getMetode());
        System.out.println("Bayar        : " + Money.rupiah(transaksi.getPembayaran().getJumlahBayar()));
        System.out.println("Kembalian    : " + Money.rupiah(transaksi.getPembayaran().getKembalian()));
        System.out.println("Status       : " + transaksi.getPembayaran().getStatus());
        System.out.println("=====================================");
    }

    private String pendek(String text, int max) {
        if (text == null) {
            return "";
        }
        if (text.length() <= max) {
            return text;
        }
        return text.substring(0, max - 3) + "...";
    }
}
