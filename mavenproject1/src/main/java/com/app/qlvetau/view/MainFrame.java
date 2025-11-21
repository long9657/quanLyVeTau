package com.app.qlvetau.view;

import com.app.qlvetau.controller.NguoiMuaController;
import com.app.qlvetau.controller.VeController;
import com.app.qlvetau.controller.HoaDonController;
import com.app.qlvetau.model.entity.NguoiMua;
import com.app.qlvetau.model.entity.Ve;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * MainFrame with tabs:
 * - "Người mua" tab: hiển thị bảng người mua
 * - "Vé" tab: hiển thị bảng vé
 * Buttons trên toolbar vẫn mở form thêm / các chức năng khác.
 */
public class MainFrame extends JFrame {
    private final NguoiMuaController nmCtrl;
    private final VeController vCtrl;
    private final HoaDonController hdCtrl;

    private final DefaultTableModel modelNguoi;
    private final DefaultTableModel modelVe;

    private final JTable tableNguoi;
    private final JTable tableVe;

    public MainFrame() {
        super("Quản lý bán vé tàu");
        nmCtrl = new NguoiMuaController();
        vCtrl = new VeController();
        hdCtrl = new HoaDonController(vCtrl.getDao());

        modelNguoi = new DefaultTableModel(new Object[]{"Mã", "Họ tên", "Địa chỉ", "Loại"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tableNguoi = new JTable(modelNguoi);
        tableNguoi.setFillsViewportHeight(true);
        tableNguoi.setShowGrid(true);
        tableNguoi.setGridColor(Color.LIGHT_GRAY);
        tableNguoi.getTableHeader().setReorderingAllowed(false);

        modelVe = new DefaultTableModel(new Object[]{"Mã", "Loại ghế", "Đơn giá"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tableVe = new JTable(modelVe);
        tableVe.setFillsViewportHeight(true);
        tableVe.setShowGrid(true);
        tableVe.setGridColor(Color.LIGHT_GRAY);
        tableVe.getTableHeader().setReorderingAllowed(false);

        initUI();
    }

    private void initUI() {
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton btnNguoi = new JButton("Quản lý Người Mua");
        JButton btnVe = new JButton("Quản lý Vé");
        JButton btnHD = new JButton("Lập Hóa Đơn");
        JButton btnSapXep = new JButton("Sắp xếp Hóa Đơn");
        JButton btnBaoCao = new JButton("Bảng kê thanh toán");

        btnNguoi.addActionListener(e -> new FormNguoiMua(this, nmCtrl).setVisible(true));
        btnVe.addActionListener(e -> new FormVe(this, vCtrl).setVisible(true));
        btnHD.addActionListener(e -> new FormHoaDon(this, nmCtrl, vCtrl, hdCtrl).setVisible(true));
        btnSapXep.addActionListener(e -> new FormSapXep(this, hdCtrl.getDao(), nmCtrl.getDao()).setVisible(true));
        btnBaoCao.addActionListener(e -> new FormBaoCao(this, hdCtrl.getDao(), nmCtrl.getDao(), vCtrl.getDao()).setVisible(true));

        top.add(btnNguoi); top.add(btnVe); top.add(btnHD); top.add(btnSapXep); top.add(btnBaoCao);
        add(top, BorderLayout.NORTH);

        // Tabbed pane with two tabs
        JTabbedPane tabs = new JTabbedPane();

        JScrollPane spNguoi = new JScrollPane(tableNguoi);
        spNguoi.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        tabs.addTab("Người mua", spNguoi);

        JScrollPane spVe = new JScrollPane(tableVe);
        spVe.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        tabs.addTab("Vé", spVe);

        add(tabs, BorderLayout.CENTER);

        // load initial data
        refreshNguoiTable();
        refreshVeTable();
    }

    // Public methods so forms can request refresh after data changes
    public void refreshNguoiTable() {
        SwingUtilities.invokeLater(() -> {
            modelNguoi.setRowCount(0);
            for (NguoiMua n : nmCtrl.listAll()) {
                modelNguoi.addRow(new Object[]{
                        String.format("%05d", n.getId()),
                        n.getHoTen(),
                        n.getDiaChi(),
                        n.getLoai()
                });
            }
        });
    }

    public void refreshVeTable() {
        SwingUtilities.invokeLater(() -> {
            modelVe.setRowCount(0);
            for (Ve v : vCtrl.listAll()) {
                modelVe.addRow(new Object[]{
                        String.format("%05d", v.getId()),
                        v.getLoaiGhe(),
                        String.format("%.2f", v.getDonGia())
                });
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            MainFrame mf = new MainFrame();
            mf.setVisible(true);
        });
    }
}