package com.bookpos.repository.memory;

import com.bookpos.model.Pelanggan;
import com.bookpos.model.User;
import com.bookpos.repository.UserRepository;

import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private final InMemoryDatabase database;

    public InMemoryUserRepository(InMemoryDatabase database) {
        this.database = database;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        for (User user : database.users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Pelanggan savePelanggan(String nama, String username, String password, String email, String alamat) {
        if (findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username sudah digunakan.");
        }

        Pelanggan pelanggan = new Pelanggan(
                database.nextUserId++,
                database.nextPelangganId++,
                nama,
                username,
                password,
                email,
                alamat);
        database.users.add(pelanggan);
        return pelanggan;
    }
}
