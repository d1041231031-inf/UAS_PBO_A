package com.bookpos.model;

import java.math.BigDecimal;

public class Buku {
    private int idBuku;
    private String judul;
    private String penulis;
    private String genre;
    private BigDecimal harga;
    private int stok;
    private static int totalObjectBuku;

    public Buku() {
        this(0, "", "", "", BigDecimal.ZERO, 0);
    }

    public Buku(String judul, String penulis, String genre, BigDecimal harga, int stok) {
        this(0, judul, penulis, genre, harga, stok);
    }

    public Buku(int idBuku, String judul, String penulis, String genre, BigDecimal harga, int stok) {
        this.idBuku = idBuku;
        this.judul = judul;
        this.penulis = penulis;
        this.genre = genre;
        this.harga = harga;
        this.stok = stok;
        totalObjectBuku++;
    }

    public boolean stokCukup(int jumlah) {
        return jumlah > 0 && stok >= jumlah;
    }

    public static int getTotalObjectBuku() {
        return totalObjectBuku;
    }

    public int getIdBuku() {
        return idBuku;
    }

    public String getJudul() {
        return judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public String getGenre() {
        return genre;
    }

    public BigDecimal getHarga() {
        return harga;
    }

    public int getStok() {
        return stok;
    }
}
