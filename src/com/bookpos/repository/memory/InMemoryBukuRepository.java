package com.bookpos.repository.memory;

import com.bookpos.model.Buku;
import com.bookpos.repository.BukuRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryBukuRepository implements BukuRepository {
    private final InMemoryDatabase database;

    public InMemoryBukuRepository(InMemoryDatabase database) {
        this.database = database;
    }

    @Override
    public List<Buku> findAll() {
        return new ArrayList<>(database.books);
    }

    @Override
    public List<Buku> search(String keyword) {
        List<Buku> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Buku buku : database.books) {
            if (buku.getJudul().toLowerCase().contains(lowerKeyword)
                    || buku.getPenulis().toLowerCase().contains(lowerKeyword)
                    || buku.getGenre().toLowerCase().contains(lowerKeyword)) {
                result.add(buku);
            }
        }
        return result;
    }

    @Override
    public Optional<Buku> findById(int idBuku) {
        for (Buku buku : database.books) {
            if (buku.getIdBuku() == idBuku) {
                return Optional.of(buku);
            }
        }
        return Optional.empty();
    }

    @Override
    public Buku save(Buku buku) {
        Buku saved = new Buku(
                database.nextBukuId++,
                buku.getJudul(),
                buku.getPenulis(),
                buku.getGenre(),
                buku.getHarga(),
                buku.getStok());
        database.books.add(saved);
        return saved;
    }

    @Override
    public boolean update(Buku buku) {
        for (int i = 0; i < database.books.size(); i++) {
            if (database.books.get(i).getIdBuku() == buku.getIdBuku()) {
                database.books.set(i, buku);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteById(int idBuku) {
        return database.books.removeIf(buku -> buku.getIdBuku() == idBuku);
    }

    @Override
    public void decreaseStock(int idBuku, int jumlah) {
        for (int i = 0; i < database.books.size(); i++) {
            Buku buku = database.books.get(i);
            if (buku.getIdBuku() == idBuku) {
                if (!buku.stokCukup(jumlah)) {
                    throw new IllegalArgumentException("Stok buku tidak cukup.");
                }
                database.books.set(i, new Buku(
                        buku.getIdBuku(),
                        buku.getJudul(),
                        buku.getPenulis(),
                        buku.getGenre(),
                        buku.getHarga(),
                        buku.getStok() - jumlah));
                return;
            }
        }
        throw new IllegalArgumentException("Buku tidak ditemukan.");
    }
}
