package com.bookpos.gui;

import com.bookpos.model.Admin;
import com.bookpos.model.Buku;
import com.bookpos.model.Pelanggan;
import com.bookpos.model.Transaksi;
import com.bookpos.service.AuthService;
import com.bookpos.service.BukuService;
import com.bookpos.service.TransaksiService;
import com.bookpos.service.TransaksiService.CartItem;
import com.bookpos.util.Money;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PosSwingFrame extends JFrame {
    private static final String HOME = "home";
    private static final String ADMIN = "admin";
    private static final String PELANGGAN = "pelanggan";

    private final AuthService authService;
    private final BukuService bukuService;
    private final TransaksiService transaksiService;
    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);

    private final DefaultTableModel adminBookModel = bookTableModel();
    private final DefaultTableModel pelangganBookModel = bookTableModel();
    private final DefaultTableModel cartModel = new DefaultTableModel(
            new String[]{"ID", "Judul", "Harga", "Jumlah", "Subtotal"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private JTable adminBookTable;
    private JTable pelangganBookTable;
    private JTable cartTable;
    private JTextField searchField;
    private Pelanggan activePelanggan;

    public PosSwingFrame(AuthService authService, BukuService bukuService, TransaksiService transaksiService) {
        this.authService = authService;
        this.bukuService = bukuService;
        this.transaksiService = transaksiService;

        setTitle("Sistem POS Penjualan Buku Online");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 640));
        setLocationRelativeTo(null);

        root.add(homePanel(), HOME);
        root.add(adminPanel(), ADMIN);
        root.add(pelangganPanel(), PELANGGAN);
        add(root);
        showHome();
    }

    private JPanel homePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        JLabel title = new JLabel("Sistem POS Penjualan Buku Online", SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
        gbc.gridy = 0;
        panel.add(title, gbc);

        JLabel subtitle = new JLabel("Java Swing - Database sementara menggunakan ArrayList", SwingConstants.CENTER);
        subtitle.setForeground(new Color(80, 80, 80));
        gbc.gridy = 1;
        panel.add(subtitle, gbc);

        JPanel buttons = new JPanel(new GridLayout(4, 1, 0, 10));
        buttons.setBorder(BorderFactory.createEmptyBorder(24, 120, 0, 120));
        buttons.add(button("Login Admin", this::loginAdmin));
        buttons.add(button("Login Pelanggan", this::loginPelanggan));
        buttons.add(button("Registrasi Pelanggan", this::registrasiPelanggan));
        buttons.add(button("Keluar", this::dispose));
        gbc.gridy = 2;
        panel.add(buttons, gbc);
        return panel;
    }

    private JPanel adminPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Dashboard Admin");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        panel.add(title, BorderLayout.NORTH);

        adminBookTable = new JTable(adminBookModel);
        panel.add(new JScrollPane(adminBookTable), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(button("Refresh", () -> refreshAdminBooks()));
        actions.add(button("Tambah Buku", this::tambahBuku));
        actions.add(button("Update Stok", this::updateStokBuku));
        actions.add(button("Hapus Buku", this::hapusBuku));
        actions.add(button("Lihat Transaksi", this::lihatSemuaTransaksi));
        actions.add(button("Logout", this::showHome));
        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel pelangganPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Dashboard Pelanggan");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        panel.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 12, 0));

        JPanel booksPanel = new JPanel(new BorderLayout(8, 8));
        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchField = new JTextField();
        searchPanel.add(new JLabel("Cari buku:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(button("Cari", this::cariBuku), BorderLayout.EAST);
        booksPanel.add(searchPanel, BorderLayout.NORTH);

        pelangganBookTable = new JTable(pelangganBookModel);
        booksPanel.add(new JScrollPane(pelangganBookTable), BorderLayout.CENTER);

        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JSpinner jumlahSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        addPanel.add(new JLabel("Jumlah:"));
        addPanel.add(jumlahSpinner);
        addPanel.add(button("Tambah ke Keranjang", () -> tambahKeKeranjang((Integer) jumlahSpinner.getValue())));
        booksPanel.add(addPanel, BorderLayout.SOUTH);

        JPanel cartPanel = new JPanel(new BorderLayout(8, 8));
        JLabel cartTitle = new JLabel("Keranjang");
        cartTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        cartPanel.add(cartTitle, BorderLayout.NORTH);
        cartTable = new JTable(cartModel);
        cartPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        JPanel cartActions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cartActions.add(button("Hapus Item", this::hapusItemKeranjang));
        cartActions.add(button("Checkout", this::checkout));
        cartActions.add(button("Riwayat", this::lihatRiwayatPelanggan));
        cartActions.add(button("Logout", this::logoutPelanggan));
        cartPanel.add(cartActions, BorderLayout.SOUTH);

        center.add(booksPanel);
        center.add(cartPanel);
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    private void loginAdmin() {
        LoginInput login = askLogin("Login Admin");
        if (login == null) {
            return;
        }

        Optional<Admin> admin = authService.loginAdmin(login.username(), login.password());
        if (admin.isEmpty()) {
            message("Login admin gagal. Gunakan akun admin yang benar.");
            return;
        }

        refreshAdminBooks();
        cards.show(root, ADMIN);
    }

    private void loginPelanggan() {
        LoginInput login = askLogin("Login Pelanggan");
        if (login == null) {
            return;
        }

        Optional<Pelanggan> pelanggan = authService.loginPelanggan(login.username(), login.password());
        if (pelanggan.isEmpty()) {
            message("Login pelanggan gagal. Gunakan akun pelanggan yang benar.");
            return;
        }

        activePelanggan = pelanggan.get();
        cartModel.setRowCount(0);
        refreshPelangganBooks(bukuService.tampilSemua());
        cards.show(root, PELANGGAN);
    }

    private void registrasiPelanggan() {
        JTextField nama = new JTextField();
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        JTextField email = new JTextField();
        JTextField alamat = new JTextField();

        JPanel form = formPanel(new String[]{"Nama", "Username", "Password", "Email", "Alamat"},
                new java.awt.Component[]{nama, username, password, email, alamat});
        int result = JOptionPane.showConfirmDialog(this, form, "Registrasi Pelanggan",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            Pelanggan pelanggan = authService.registerPelanggan(
                    nama.getText().trim(),
                    username.getText().trim(),
                    new String(password.getPassword()),
                    email.getText().trim(),
                    alamat.getText().trim());
            message("Registrasi berhasil. ID pelanggan: " + pelanggan.getIdPelanggan());
        } catch (IllegalArgumentException exception) {
            message(exception.getMessage());
        }
    }

    private void tambahBuku() {
        JTextField judul = new JTextField();
        JTextField penulis = new JTextField();
        JTextField genre = new JTextField();
        JTextField harga = new JTextField();
        JSpinner stok = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));

        JPanel form = formPanel(new String[]{"Judul", "Penulis", "Genre", "Harga", "Stok"},
                new java.awt.Component[]{judul, penulis, genre, harga, stok});
        int result = JOptionPane.showConfirmDialog(this, form, "Tambah Buku",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            bukuService.tambah(judul.getText().trim(), penulis.getText().trim(), genre.getText().trim(),
                    new BigDecimal(harga.getText().trim()), (Integer) stok.getValue());
            refreshAdminBooks();
            message("Buku berhasil ditambahkan.");
        } catch (NumberFormatException exception) {
            message("Harga harus berupa angka.");
        } catch (IllegalArgumentException exception) {
            message(exception.getMessage());
        }
    }

    private void updateStokBuku() {
        Integer id = selectedId(adminBookTable);
        if (id == null) {
            message("Pilih buku yang akan diupdate stoknya.");
            return;
        }

        JSpinner stok = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
        int result = JOptionPane.showConfirmDialog(this, stok, "Stok Baru",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        if (bukuService.updateStok(id, (Integer) stok.getValue())) {
            refreshAdminBooks();
            message("Stok buku berhasil diupdate.");
        } else {
            message("Buku tidak ditemukan.");
        }
    }

    private void hapusBuku() {
        Integer id = selectedId(adminBookTable);
        if (id == null) {
            message("Pilih buku yang akan dihapus.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Hapus buku terpilih?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION && bukuService.hapus(id)) {
            refreshAdminBooks();
            message("Buku berhasil dihapus.");
        }
    }

    private void cariBuku() {
        String keyword = searchField.getText().trim();
        if (keyword.isBlank()) {
            refreshPelangganBooks(bukuService.tampilSemua());
        } else {
            refreshPelangganBooks(bukuService.cari(keyword));
        }
    }

    private void tambahKeKeranjang(int jumlah) {
        Integer id = selectedId(pelangganBookTable);
        if (id == null) {
            message("Pilih buku terlebih dahulu.");
            return;
        }

        Optional<Buku> buku = bukuService.cariById(id);
        if (buku.isEmpty()) {
            message("Buku tidak ditemukan.");
            return;
        }

        if (!buku.get().stokCukup(jumlah)) {
            message("Stok buku tidak cukup.");
            return;
        }

        BigDecimal subtotal = buku.get().getHarga().multiply(BigDecimal.valueOf(jumlah));
        cartModel.addRow(new Object[]{id, buku.get().getJudul(), buku.get().getHarga(), jumlah, subtotal});
    }

    private void hapusItemKeranjang() {
        int row = cartTable.getSelectedRow();
        if (row >= 0) {
            cartModel.removeRow(cartTable.convertRowIndexToModel(row));
        }
    }

    private void checkout() {
        if (cartModel.getRowCount() == 0) {
            message("Keranjang masih kosong.");
            return;
        }

        List<CartItem> cartItems = cartItemsFromTable();
        BigDecimal total = cartTotal();
        PaymentInput payment = askPayment(total);
        if (payment == null) {
            return;
        }

        try {
            Transaksi transaksi = transaksiService.checkout(activePelanggan, cartItems,
                    payment.metode(), total);
            cartModel.setRowCount(0);
            refreshPelangganBooks(bukuService.tampilSemua());
            showText("Struk Transaksi", receipt(transaksi));
        } catch (IllegalArgumentException exception) {
            message(exception.getMessage());
        }
    }

    private PaymentInput askPayment(BigDecimal total) {
        JComboBox<String> metode = new JComboBox<>(new String[]{"Transfer Bank", "E-Wallet"});
        JPanel form = formPanel(new String[]{"Total", "Metode", "Nominal Bayar"},
                new java.awt.Component[]{new JLabel(Money.rupiah(total)), metode, new JLabel(Money.rupiah(total))});
        int result = JOptionPane.showConfirmDialog(this, form, "Pembayaran",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }
        return new PaymentInput((String) metode.getSelectedItem());
    }

    private void lihatSemuaTransaksi() {
        showTransactions("Data Transaksi", transaksiService.semuaTransaksi());
    }

    private void lihatRiwayatPelanggan() {
        showTransactions("Riwayat Transaksi", transaksiService.riwayatPelanggan(activePelanggan));
    }

    private void showTransactions(String title, List<Transaksi> transactions) {
        if (transactions.isEmpty()) {
            message("Belum ada transaksi.");
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (Transaksi transaksi : transactions) {
            builder.append(receipt(transaksi)).append(System.lineSeparator());
        }
        showText(title, builder.toString());
    }

    private void logoutPelanggan() {
        activePelanggan = null;
        cartModel.setRowCount(0);
        showHome();
    }

    private void showHome() {
        cards.show(root, HOME);
    }

    private void refreshAdminBooks() {
        fillBookModel(adminBookModel, bukuService.tampilSemua());
    }

    private void refreshPelangganBooks(List<Buku> books) {
        fillBookModel(pelangganBookModel, books);
    }

    private void fillBookModel(DefaultTableModel model, List<Buku> books) {
        model.setRowCount(0);
        for (Buku buku : books) {
            model.addRow(new Object[]{
                    buku.getIdBuku(),
                    buku.getJudul(),
                    buku.getPenulis(),
                    buku.getGenre(),
                    buku.getHarga(),
                    buku.getStok()
            });
        }
    }

    private DefaultTableModel bookTableModel() {
        return new DefaultTableModel(new String[]{"ID", "Judul", "Penulis", "Genre", "Harga", "Stok"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private List<CartItem> cartItemsFromTable() {
        Map<Integer, Integer> grouped = new LinkedHashMap<>();
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            int id = (Integer) cartModel.getValueAt(i, 0);
            int jumlah = (Integer) cartModel.getValueAt(i, 3);
            grouped.merge(id, jumlah, Integer::sum);
        }

        List<CartItem> items = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : grouped.entrySet()) {
            items.add(new CartItem(entry.getKey(), entry.getValue()));
        }
        return items;
    }

    private BigDecimal cartTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            total = total.add((BigDecimal) cartModel.getValueAt(i, 4));
        }
        return total;
    }

    private String receipt(Transaksi transaksi) {
        StringBuilder builder = new StringBuilder();
        builder.append("========== STRUK TRANSAKSI ==========").append(System.lineSeparator());
        builder.append("ID Transaksi : ").append(transaksi.getIdTransaksi()).append(System.lineSeparator());
        builder.append("Tanggal      : ")
                .append(transaksi.getTanggal().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))
                .append(System.lineSeparator());
        builder.append("Pelanggan    : ").append(transaksi.getPelanggan().getNama()).append(System.lineSeparator());
        builder.append("-------------------------------------").append(System.lineSeparator());
        for (Transaksi.ItemPembelian item : transaksi.getItems()) {
            builder.append(item.getBuku().getJudul())
                    .append(" x").append(item.getJumlah())
                    .append(" = ").append(Money.rupiah(item.hitungSubtotal()))
                    .append(System.lineSeparator());
        }
        builder.append("-------------------------------------").append(System.lineSeparator());
        builder.append("Total item   : ").append(transaksi.totalItemArrayDemo()).append(System.lineSeparator());
        builder.append("Total        : ").append(Money.rupiah(transaksi.hitungTotal())).append(System.lineSeparator());
        builder.append("Metode       : ").append(transaksi.getPembayaran().getMetode()).append(System.lineSeparator());
        builder.append("Bayar        : ").append(Money.rupiah(transaksi.getPembayaran().getJumlahBayar())).append(System.lineSeparator());
        builder.append("Kembalian    : ").append(Money.rupiah(transaksi.getPembayaran().getKembalian())).append(System.lineSeparator());
        builder.append("Status       : ").append(transaksi.getPembayaran().getStatus()).append(System.lineSeparator());
        builder.append("=====================================").append(System.lineSeparator());
        return builder.toString();
    }

    private LoginInput askLogin(String title) {
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        JPanel form = formPanel(new String[]{"Username", "Password"},
                new java.awt.Component[]{username, password});
        int result = JOptionPane.showConfirmDialog(this, form, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }
        return new LoginInput(username.getText().trim(), new String(password.getPassword()));
    }

    private JPanel formPanel(String[] labels, java.awt.Component[] fields) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0;
            panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1;
            fields[i].setPreferredSize(new Dimension(240, 28));
            panel.add(fields[i], gbc);
        }
        return panel;
    }

    private JButton button(String text, Runnable action) {
        JButton button = new JButton(text);
        button.addActionListener(event -> action.run());
        return button;
    }

    private Integer selectedId(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            return null;
        }
        return (Integer) table.getModel().getValueAt(table.convertRowIndexToModel(row), 0);
    }

    private void message(String text) {
        JOptionPane.showMessageDialog(this, text);
    }

    private void showText(String title, String text) {
        JTextArea area = new JTextArea(text, 24, 64);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JDialog dialog = new JDialog(this, title, true);
        dialog.add(new JScrollPane(area));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private record LoginInput(String username, String password) {
    }

    private record PaymentInput(String metode) {
    }
}
