package com.bookpos.service;

import com.bookpos.model.Admin;
import com.bookpos.model.Pelanggan;
import com.bookpos.model.User;
import com.bookpos.repository.UserRepository;

import java.util.Optional;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().passwordMatches(password)) {
            return user;
        }
        return Optional.empty();
    }

    public Optional<Admin> loginAdmin(String username, String password) {
        Optional<User> user = login(username, password);
        if (user.isPresent() && user.get() instanceof Admin admin) {
            return Optional.of(admin);
        }
        return Optional.empty();
    }

    public Optional<Pelanggan> loginPelanggan(String username, String password) {
        Optional<User> user = login(username, password);
        if (user.isPresent() && user.get() instanceof Pelanggan pelanggan) {
            return Optional.of(pelanggan);
        }
        return Optional.empty();
    }

    public Pelanggan registerPelanggan(String nama, String username, String password, String email, String alamat) {
        if (nama.isBlank() || username.isBlank() || password.isBlank() || email.isBlank()) {
            throw new IllegalArgumentException("Nama, username, password, dan email wajib diisi.");
        }
        return userRepository.savePelanggan(nama, username, password, email, alamat);
    }
}
