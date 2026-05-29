package com.bookpos.model;

public class Pelanggan extends User {
    private int idPelanggan;
    private String alamat;

    public Pelanggan(int id, int idPelanggan, String nama, String username, String password, String email, String alamat) {
        super(id, nama, username, password, email, Role.PELANGGAN);
        this.idPelanggan = idPelanggan;
        this.alamat = alamat;
    }

    @Override
    public String menuTitle() {
        return "Menu Pelanggan";
    }

    public int getIdPelanggan() {
        return idPelanggan;
    }

    public String getAlamat() {
        return alamat;
    }
}
