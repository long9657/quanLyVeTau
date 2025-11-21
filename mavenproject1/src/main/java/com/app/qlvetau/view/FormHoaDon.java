package com.app.qlvetau.view;

import com.app.qlvetau.controller.HoaDonController;
import com.app.qlvetau.controller.NguoiMuaController;
import com.app.qlvetau.controller.VeController;
import com.app.qlvetau.model.entity.ChiTietHD;
import com.app.qlvetau.model.entity.HoaDon;
import com.app.qlvetau.model.entity.NguoiMua;
import com.app.qlvetau.model.entity.Ve;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class FormHoaDon extends JDialog {
    private final NguoiMuaController nmCtrl;
    private final VeController vCtrl;
    private final HoaDonController hdCtrl;

    private final JComboBox<NguoiMua> cbNguoi = new JComboBox<>();
    private final JComboBox<Ve> cbVe = new JComboBox<>();
    private final JSpinner spSoLuong = new JSpinner(new SpinnerNumberModel(1,1,20,1));
    private final DefaultListModel<ChiTietHD> modelChiTiet = new DefaultListModel<>();
    private final JList<ChiTietHD> listCt = new JList<>(modelChiTiet);
    private final JLabel lblStatus = new JLabel(" ");

    public FormHoaDon(Frame owner, NguoiMuaController nm, VeController v, HoaDonController hd) {
        super(owner, "Lập Hóa Đơn", true);
        this.nmCtrl = nm;
        this.vCtrl = v;
        this.hdCtrl = hd;
        initUI();
        setSize(750, 520);
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel top = new JPanel(new GridLayout(1,2,6,6));
        nmCtrl.listAll().forEach(cbNguoi::addItem);
        top.add(new JLabel("Người mua:")); top.add(cbNguoi);

        JPanel center = new JPanel(new BorderLayout(6,6));
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        vCtrl.listAll().forEach(cbVe::addItem);
        addPanel.add(new JLabel("Vé:")); addPanel.add(cbVe);
        addPanel.add(new JLabel("Số lượng:")); addPanel.add(spSoLuong);
        JButton btnAddCt = new JButton("Thêm chi tiết");
        JButton btnRemoveCt = new JButton("Xóa chi tiết");
        addPanel.add(btnAddCt); addPanel.add(btnRemoveCt);

        listCt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        center.add(addPanel, BorderLayout.NORTH);
        center.add(new JScrollPane(listCt), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnLuu = new JButton("Lưu Hóa Đơn");
        JButton btnTinh = new JButton("Tính Tổng (trước khi lưu)");
        bottom.add(btnLuu); bottom.add(btnTinh); bottom.add(lblStatus);

        btnAddCt.addActionListener(e -> {
            if (modelChiTiet.size() >= 4) {
                JOptionPane.showMessageDialog(this, "Không quá 4 loại vé trong 1 hóa đơn", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Ve v = (Ve) cbVe.getSelectedItem();
            int q = (int) spSoLuong.getValue();
            if (v == null) {
                JOptionPane.showMessageDialog(this, "Không có vé để chọn", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // if same veId exists, merge quantities (optional) -> here we merge to avoid duplicate ve rows
            boolean merged = false;
            for (int i=0;i<modelChiTiet.size();i++) {
                ChiTietHD ct = modelChiTiet.get(i);
                if (ct.getVeId() == v.getId()) {
                    int newQ = ct.getSoLuong() + q;
                    if (newQ > 20) {
                        JOptionPane.showMessageDialog(this, "Tổng số lượng cho 1 loại không vượt quá 20", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    ct.setSoLuong(newQ);
                    modelChiTiet.set(i, ct);
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                modelChiTiet.addElement(new ChiTietHD(v.getId(), q));
            }
        });

        btnRemoveCt.addActionListener(e -> {
            int idx = listCt.getSelectedIndex();
            if (idx >= 0) modelChiTiet.remove(idx);
        });

        btnTinh.addActionListener(e -> {
            try {
                HoaDon hd = buildHoaDonFromUI(false);
                double total = hdCtrl.calculateTotal(hd);
                lblStatus.setText(String.format("Tổng: %.2f", total));
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLuu.addActionListener(e -> {
            try {
                HoaDon hd = buildHoaDonFromUI(true);
                hdCtrl.addHoaDon(hd);
                double total = hdCtrl.calculateTotal(hd);
                lblStatus.setText(String.format("Đã lưu. Tổng: %.2f", total));
                modelChiTiet.clear();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi lưu file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        setLayout(new BorderLayout(8,8));
        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        refreshLists();
    }

    private HoaDon buildHoaDonFromUI(boolean forSave) {
        NguoiMua nm = (NguoiMua) cbNguoi.getSelectedItem();
        if (nm == null) throw new IllegalArgumentException("Chọn người mua");
        if (modelChiTiet.isEmpty()) throw new IllegalArgumentException("Hóa đơn phải có chi tiết");
        if (modelChiTiet.size() > 4) throw new IllegalArgumentException("Không quá 4 loại vé");
        HoaDon hd = new HoaDon(0, nm.getId());
        for (int i=0;i<modelChiTiet.size();i++) {
            ChiTietHD ct = modelChiTiet.get(i);
            if (ct.getSoLuong() <=0 || ct.getSoLuong() > 20) throw new IllegalArgumentException("Số lượng phải 1..20");
            hd.addChiTiet(new ChiTietHD(ct.getVeId(), ct.getSoLuong()));
        }
        return hd;
    }

    private void refreshLists() {
        cbNguoi.removeAllItems();
        nmCtrl.listAll().forEach(cbNguoi::addItem);
        cbVe.removeAllItems();
        vCtrl.listAll().forEach(cbVe::addItem);
    }
}