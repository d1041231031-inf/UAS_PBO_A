package com.bookpos.repository;

import com.bookpos.model.Pelanggan;
import com.bookpos.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    Pelanggan savePelanggan(String nama, String username, String password, String email, String alamat);
}
