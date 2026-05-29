package com.bookpos.repository.memory;

import com.bookpos.model.Admin;
import com.bookpos.model.Buku;
import com.bookpos.model.Pelanggan;
import com.bookpos.model.Transaksi;
import com.bookpos.model.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InMemoryDatabase {
    final List<User> users = new ArrayList<>();
    final List<Buku> books = new ArrayList<>();
    final List<Transaksi> transactions = new ArrayList<>();

    int nextUserId = 1;
    int nextPelangganId = 1;
    int nextBukuId = 1;
    int nextTransaksiId = 1;

    public static InMemoryDatabase withSeedData() {
        InMemoryDatabase database = new InMemoryDatabase();
        database.seedUsers();
        database.seedBooks();
        return database;
    }

    private void seedUsers() {
        users.add(new Admin(nextUserId++, "Administrator Toko", "admin", "admin123", "admin@bookpos.local"));
        users.add(new Pelanggan(nextUserId++, nextPelangganId++, "Rayhan Nuerjamman", "rayhan", "rayhan123",
                "rayhan@bookpos.local", "Pontianak"));
        users.add(new Pelanggan(nextUserId++, nextPelangganId++, "Arif Fadhilah", "arif", "arif123",
                "arif@bookpos.local", "Pontianak"));
    }

    private void seedBooks() {
        books.add(new Buku(nextBukuId++, "Pemrograman Java Dasar", "Andi Nugroho",
                "Pemrograman", new BigDecimal("85000"), 12));
        books.add(new Buku(nextBukuId++, "Object Oriented Programming", "Dewi Lestari",
                "Pemrograman", new BigDecimal("95000"), 8));
        books.add(new Buku(nextBukuId++, "Basis Data dengan Java", "Budi Santoso",
                "Pemrograman", new BigDecimal("78000"), 10));
        books.add(new Buku(nextBukuId++, "Algoritma dan Struktur Data", "Siti Rahma",
                "Informatika", new BigDecimal("88000"), 7));
        books.add(new Buku(nextBukuId++, "Analisis dan Perancangan Sistem", "Rina Putri",
                "Sistem Informasi", new BigDecimal("90000"), 6));
    }
}
