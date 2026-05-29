package com.bookpos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Transaksi {
    private int idTransaksi;
    private final Pelanggan pelanggan;
    private final LocalDateTime tanggal;
    private final List<ItemPembelian> items;
    private Pembayaran pembayaran;

    public Transaksi(Pelanggan pelanggan) {
        this(0, pelanggan, LocalDateTime.now(), new ArrayList<ItemPembelian>(), null);
    }

    public Transaksi(int idTransaksi, Pelanggan pelanggan, LocalDateTime tanggal,
                     List<ItemPembelian> items, Pembayaran pembayaran) {
        this.idTransaksi = idTransaksi;
        this.pelanggan = pelanggan;
        this.tanggal = tanggal;
        this.items = items;
        this.pembayaran = pembayaran;
    }

    public void tambahItem(Buku buku, int jumlah) {
        items.add(new ItemPembelian(buku, jumlah));
    }

    public BigDecimal hitungTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemPembelian item : items) {
            total = total.add(item.hitungSubtotal());
        }
        return total;
    }

    public int totalItemArrayDemo() {
        int[] jumlahPerItem = new int[items.size()];
        for (int i = 0; i < items.size(); i++) {
            jumlahPerItem[i] = items.get(i).getJumlah();
        }

        int total = 0;
        for (int jumlah : jumlahPerItem) {
            total += jumlah;
        }
        return total;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public List<ItemPembelian> getItems() {
        return Collections.unmodifiableList(items);
    }

    public Pembayaran getPembayaran() {
        return pembayaran;
    }

    public void setPembayaran(Pembayaran pembayaran) {
        this.pembayaran = pembayaran;
    }

    public static class ItemPembelian {
        private final Buku buku;
        private final int jumlah;

        public ItemPembelian(Buku buku, int jumlah) {
            this.buku = buku;
            this.jumlah = jumlah;
        }

        public BigDecimal hitungSubtotal() {
            return buku.getHarga().multiply(BigDecimal.valueOf(jumlah));
        }

        public Buku getBuku() {
            return buku;
        }

        public int getJumlah() {
            return jumlah;
        }
    }
}
