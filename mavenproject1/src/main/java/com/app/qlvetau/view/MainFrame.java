package com.app.qlvetau.view;

import com.app.qlvetau.controller.NguoiMuaController;
import com.app.qlvetau.controller.VeController;
import com.app.qlvetau.controller.HoaDonController;
import com.app.qlvetau.model.entity.NguoiMua;
import com.app.qlvetau.model.entity.Ve;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final NguoiMuaController nmCtrl;
    private final VeController vCtrl;
    private final HoaDonController hdCtrl;

    private final DefaultListModel<NguoiMua> modelNguoi = new DefaultListModel<>();
    private final DefaultListModel<Ve> modelVe = new DefaultListModel<>();

    public MainFrame() {
        super("Quản lý bán vé tàu");
        nmCtrl = new NguoiMuaController();
        vCtrl = new VeController();
        hdCtrl = new HoaDonController(vCtrl.getDao());
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

        // Center: two lists side by side (NguoiMua | Ve)
        JPanel center = new JPanel(new GridLayout(1, 2, 8, 8));

        // Người mua panel
        JPanel pNguoi = new JPanel(new BorderLayout(4,4));
        pNguoi.setBorder(BorderFactory.createTitledBorder("Danh sách Người mua"));
        JList<NguoiMua> listNguoi = new JList<>(modelNguoi);
        pNguoi.add(new JScrollPane(listNguoi), BorderLayout.CENTER);
        center.add(pNguoi);

        // Vé panel
        JPanel pVe = new JPanel(new BorderLayout(4,4));
        pVe.setBorder(BorderFactory.createTitledBorder("Danh sách Vé"));
        JList<Ve> listVe = new JList<>(modelVe);
        pVe.add(new JScrollPane(listVe), BorderLayout.CENTER);
        center.add(pVe);

        add(center, BorderLayout.CENTER);

        // load initial data
        refreshNguoiList();
        refreshVeList();
    }

    // Public methods so forms can request refresh after data changes
    public void refreshNguoiList() {
        SwingUtilities.invokeLater(() -> {
            modelNguoi.clear();
            nmCtrl.listAll().forEach(modelNguoi::addElement);
        });
    }

    public void refreshVeList() {
        SwingUtilities.invokeLater(() -> {
            modelVe.clear();
            vCtrl.listAll().forEach(modelVe::addElement);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // optional LAF
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            MainFrame mf = new MainFrame();
            mf.setVisible(true);
        });
    }
}