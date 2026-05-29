package com.bookpos;

import com.bookpos.repository.BukuRepository;
import com.bookpos.repository.TransaksiRepository;
import com.bookpos.repository.UserRepository;
import com.bookpos.repository.memory.InMemoryBukuRepository;
import com.bookpos.repository.memory.InMemoryDatabase;
import com.bookpos.repository.memory.InMemoryTransaksiRepository;
import com.bookpos.repository.memory.InMemoryUserRepository;
import com.bookpos.service.AuthService;
import com.bookpos.service.BukuService;
import com.bookpos.service.TransaksiService;
import com.bookpos.ui.ConsoleMenu;

public class App {
    public static void main(String[] args) {
        InMemoryDatabase database = InMemoryDatabase.withSeedData();

        UserRepository userRepository = new InMemoryUserRepository(database);
        BukuRepository bukuRepository = new InMemoryBukuRepository(database);
        TransaksiRepository transaksiRepository = new InMemoryTransaksiRepository(database);

        AuthService authService = new AuthService(userRepository);
        BukuService bukuService = new BukuService(bukuRepository);
        TransaksiService transaksiService = new TransaksiService(bukuRepository, transaksiRepository);

        ConsoleMenu menu = new ConsoleMenu(authService, bukuService, transaksiService);
        menu.run();
    }
}
