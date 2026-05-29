package com.bookpos.model;

import java.math.BigDecimal;

public class Pembayaran {
    private final int idPembayaran;
    private final String metode;
    private final BigDecimal jumlahBayar;
    private final BigDecimal kembalian;
    private final StatusPembayaran status;

    public Pembayaran(String metode, BigDecimal jumlahBayar, BigDecimal total) {
        this(0, metode, jumlahBayar, jumlahBayar.subtract(total),
                jumlahBayar.compareTo(total) >= 0 ? StatusPembayaran.LUNAS : StatusPembayaran.GAGAL);
    }

    public Pembayaran(int idPembayaran, String metode, BigDecimal jumlahBayar,
                      BigDecimal kembalian, StatusPembayaran status) {
        this.idPembayaran = idPembayaran;
        this.metode = metode;
        this.jumlahBayar = jumlahBayar;
        this.kembalian = kembalian;
        this.status = status;
    }

    public boolean berhasil() {
        return status == StatusPembayaran.LUNAS;
    }

    public int getIdPembayaran() {
        return idPembayaran;
    }

    public String getMetode() {
        return metode;
    }

    public BigDecimal getJumlahBayar() {
        return jumlahBayar;
    }

    public BigDecimal getKembalian() {
        return kembalian;
    }

    public StatusPembayaran getStatus() {
        return status;
    }
}
