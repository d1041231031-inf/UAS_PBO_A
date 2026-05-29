package com.bookpos.model;

public abstract class User {
    private int id;
    private String nama;
    private String username;
    private String password;
    private String email;
    private Role role;

    protected User(int id, String nama, String username, String password, String email, Role role) {
        this.id = id;
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public abstract String menuTitle();

    public boolean passwordMatches(String inputPassword) {
        return password.equals(inputPassword);
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}
