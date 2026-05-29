package com.bookpos.repository.memory;

import com.bookpos.model.Transaksi;
import com.bookpos.repository.TransaksiRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTransaksiRepository implements TransaksiRepository {
    private final InMemoryDatabase database;

    public InMemoryTransaksiRepository(InMemoryDatabase database) {
        this.database = database;
    }

    @Override
    public Transaksi save(Transaksi transaksi) {
        transaksi.setIdTransaksi(database.nextTransaksiId++);
        database.transactions.add(transaksi);
        return transaksi;
    }

    @Override
    public List<Transaksi> findAll() {
        return new ArrayList<>(database.transactions);
    }

    @Override
    public List<Transaksi> findByPelangganId(int idPelanggan) {
        List<Transaksi> result = new ArrayList<>();
        for (Transaksi transaksi : database.transactions) {
            if (transaksi.getPelanggan().getIdPelanggan() == idPelanggan) {
                result.add(transaksi);
            }
        }
        return result;
    }
}
