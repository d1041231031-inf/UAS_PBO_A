package com.bookpos.repository;

import com.bookpos.model.Buku;

import java.util.List;
import java.util.Optional;

public interface BukuRepository {
    List<Buku> findAll();

    List<Buku> search(String keyword);

    Optional<Buku> findById(int idBuku);

    Buku save(Buku buku);

    boolean update(Buku buku);

    boolean deleteById(int idBuku);

    void decreaseStock(int idBuku, int jumlah);
}
