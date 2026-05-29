package com.bookpos.service;

import com.bookpos.model.Buku;
import com.bookpos.model.Pelanggan;
import com.bookpos.model.Pembayaran;
import com.bookpos.model.Transaksi;
import com.bookpos.repository.BukuRepository;
import com.bookpos.repository.TransaksiRepository;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TransaksiService {
    private final BukuRepository bukuRepository;
    private final TransaksiRepository transaksiRepository;

    public TransaksiService(BukuRepository bukuRepository, TransaksiRepository transaksiRepository) {
        this.bukuRepository = bukuRepository;
        this.transaksiRepository = transaksiRepository;
    }

    public Transaksi checkout(Pelanggan pelanggan, List<CartItem> cartItems, String metode, BigDecimal jumlahBayar) {
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Keranjang masih kosong.");
        }

        Map<Integer, Integer> jumlahPerBuku = new LinkedHashMap<>();
        for (CartItem cartItem : cartItems) {
            jumlahPerBuku.merge(cartItem.idBuku(), cartItem.jumlah(), Integer::sum);
        }

        Transaksi transaksi = new Transaksi(pelanggan);
        for (Map.Entry<Integer, Integer> entry : jumlahPerBuku.entrySet()) {
            Buku buku = bukuRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Buku ID " + entry.getKey() + " tidak ditemukan."));
            if (!buku.stokCukup(entry.getValue())) {
                throw new IllegalArgumentException("Stok buku \"" + buku.getJudul() + "\" tidak cukup.");
            }
            transaksi.tambahItem(buku, entry.getValue());
        }

        Pembayaran pembayaran = new Pembayaran(metode, jumlahBayar, transaksi.hitungTotal());
        if (!pembayaran.berhasil()) {
            throw new IllegalArgumentException("Jumlah bayar kurang dari total transaksi.");
        }

        transaksi.setPembayaran(pembayaran);
        for (Transaksi.ItemPembelian item : transaksi.getItems()) {
            bukuRepository.decreaseStock(item.getBuku().getIdBuku(), item.getJumlah());
        }
        return transaksiRepository.save(transaksi);
    }

    public List<Transaksi> semuaTransaksi() {
        return transaksiRepository.findAll();
    }

    public List<Transaksi> riwayatPelanggan(Pelanggan pelanggan) {
        return transaksiRepository.findByPelangganId(pelanggan.getIdPelanggan());
    }

    public record CartItem(int idBuku, int jumlah) {
        public CartItem {
            if (jumlah <= 0) {
                throw new IllegalArgumentException("Jumlah beli harus lebih dari 0.");
            }
        }
    }
}
