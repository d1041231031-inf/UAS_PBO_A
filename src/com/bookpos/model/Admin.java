package com.bookpos.model;

public class Admin extends User {
    public Admin(int id, String nama, String username, String password, String email) {
        super(id, nama, username, password, email, Role.ADMIN);
    }

    @Override
    public String menuTitle() {
        return "Menu Admin";
    }
}
