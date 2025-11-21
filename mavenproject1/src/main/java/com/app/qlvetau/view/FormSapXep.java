package com.app.qlvetau.view;

import com.app.qlvetau.controller.SortController;
import com.app.qlvetau.dao.HoaDonDAO;
import com.app.qlvetau.dao.NguoiMuaDAO;
import com.app.qlvetau.model.entity.HoaDon;
import com.app.qlvetau.model.entity.NguoiMua;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FormSapXep extends JDialog {
    private final HoaDonDAO hdDao;
    private final NguoiMuaDAO nmDao;
    private final DefaultListModel<String> model = new DefaultListModel<>();

    public FormSapXep(Frame owner, HoaDonDAO hdDao, NguoiMuaDAO nmDao) {
        super(owner, "Sắp xếp Hóa Đơn", true);
        this.hdDao = hdDao;
        this.nmDao = nmDao;
        initUI();
        setSize(700, 480);
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JRadioButton rbName = new JRadioButton("Theo Họ tên người mua");
        JRadioButton rbQty = new JRadioButton("Theo Số lượng vé (giảm dần)");
        ButtonGroup bg = new ButtonGroup(); bg.add(rbName); bg.add(rbQty);
        JButton btnSort = new JButton("Sắp xếp");
        JList<String> lst = new JList<>(model);

        btnSort.addActionListener(e -> {
            model.clear();
            SortController sc = new SortController(hdDao, nmDao);
            List<HoaDon> res;
            if (rbName.isSelected()) res = sc.sortByNguoiMuaName();
            else if (rbQty.isSelected()) res = sc.sortBySoLuongDesc();
            else {
                JOptionPane.showMessageDialog(this, "Chọn kiểu sắp xếp", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (HoaDon hd : res) {
                NguoiMua nm = nmDao.getAll().stream().filter(x -> x.getId() == hd.getNguoiMuaId()).findFirst().orElse(null);
                model.addElement(String.format("%s - %s - SL:%d", hd, nm == null ? "?" : nm.getHoTen(), hd.getTotalQuantity()));
            }
        });

        JPanel top = new JPanel(new FlowLayout());
        top.add(rbName); top.add(rbQty); top.add(btnSort);
        setLayout(new BorderLayout(6,6));
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(lst), BorderLayout.CENTER);
    }
}