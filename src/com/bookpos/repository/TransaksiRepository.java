package com.bookpos.repository;

import com.bookpos.model.Transaksi;

import java.util.List;

public interface TransaksiRepository {
    Transaksi save(Transaksi transaksi);

    List<Transaksi> findAll();

    List<Transaksi> findByPelangganId(int idPelanggan);
}
