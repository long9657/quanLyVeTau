package com.app.qlvetau.view;

import com.app.qlvetau.controller.BaoCaoController;
import com.app.qlvetau.dao.HoaDonDAO;
import com.app.qlvetau.dao.NguoiMuaDAO;
import com.app.qlvetau.dao.VeDAO;
import com.app.qlvetau.model.entity.NguoiMua;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class FormBaoCao extends JDialog {
    private final HoaDonDAO hdDao;
    private final NguoiMuaDAO nmDao;
    private final VeDAO veDao;
    private final DefaultListModel<String> model = new DefaultListModel<>();

    public FormBaoCao(Frame owner, HoaDonDAO hdDao, NguoiMuaDAO nmDao, VeDAO veDao) {
        super(owner, "Bảng kê thanh toán", true);
        this.hdDao = hdDao;
        this.nmDao = nmDao;
        this.veDao = veDao;
        initUI();
        setSize(700, 480);
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JButton btnGen = new JButton("Lập bảng kê");
        JList<String> lst = new JList<>(model);
        btnGen.addActionListener(e -> {
            model.clear();
            BaoCaoController bc = new BaoCaoController(hdDao, nmDao, veDao);
            Map<NguoiMua, Double> map = bc.bangKeTheoNguoi();
            if (map.isEmpty()) model.addElement("Không có hóa đơn để lập bảng kê.");
            else {
                for (var en : map.entrySet()) {
                    model.addElement(String.format("%s -> Tổng phải trả: %.2f", en.getKey(), en.getValue()));
                }
            }
        });

        setLayout(new BorderLayout(6,6));
        add(btnGen, BorderLayout.NORTH);
        add(new JScrollPane(lst), BorderLayout.CENTER);
    }
}