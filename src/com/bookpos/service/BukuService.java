package com.bookpos.service;

import com.bookpos.model.Buku;
import com.bookpos.repository.BukuRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class BukuService {
    private final BukuRepository bukuRepository;

    public BukuService(BukuRepository bukuRepository) {
        this.bukuRepository = bukuRepository;
    }

    public List<Buku> tampilSemua() {
        return bukuRepository.findAll();
    }

    public List<Buku> cari(String keyword) {
        return bukuRepository.search(keyword);
    }

    public Optional<Buku> cariById(int idBuku) {
        return bukuRepository.findById(idBuku);
    }

    public Buku tambah(String judul, String penulis, String genre, BigDecimal harga, int stok) {
        validasiBuku(judul, penulis, harga, stok);
        return bukuRepository.save(new Buku(judul, penulis, genre, harga, stok));
    }

    public boolean ubah(int idBuku, String judul, String penulis, String genre, BigDecimal harga, int stok) {
        validasiBuku(judul, penulis, harga, stok);
        return bukuRepository.update(new Buku(idBuku, judul, penulis, genre, harga, stok));
    }

    public boolean updateStok(int idBuku, int stokBaru) {
        if (stokBaru < 0) {
            throw new IllegalArgumentException("Stok tidak boleh negatif.");
        }

        Optional<Buku> buku = bukuRepository.findById(idBuku);
        if (buku.isEmpty()) {
            return false;
        }

        Buku dataLama = buku.get();
        return bukuRepository.update(new Buku(
                dataLama.getIdBuku(),
                dataLama.getJudul(),
                dataLama.getPenulis(),
                dataLama.getGenre(),
                dataLama.getHarga(),
                stokBaru));
    }

    public boolean hapus(int idBuku) {
        return bukuRepository.deleteById(idBuku);
    }

    private void validasiBuku(String judul, String penulis, BigDecimal harga, int stok) {
        if (judul.isBlank() || penulis.isBlank()) {
            throw new IllegalArgumentException("Judul dan penulis wajib diisi.");
        }
        if (harga.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Harga harus lebih dari 0.");
        }
        if (stok < 0) {
            throw new IllegalArgumentException("Stok tidak boleh negatif.");
        }
    }
}
