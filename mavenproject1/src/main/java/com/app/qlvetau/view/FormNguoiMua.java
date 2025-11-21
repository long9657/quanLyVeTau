package com.app.qlvetau.view;

import com.app.qlvetau.controller.NguoiMuaController;
import com.app.qlvetau.model.entity.NguoiMua;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class FormNguoiMua extends JDialog {
    private final NguoiMuaController controller;
    private final MainFrame mainFrame; // reference to main to trigger refresh
    private final DefaultListModel<NguoiMua> listModel = new DefaultListModel<>();

    public FormNguoiMua(MainFrame owner, NguoiMuaController controller) {
        super(owner, "Quản lý Người Mua", true);
        this.mainFrame = owner;
        this.controller = controller;
        initUI();
        setSize(600, 420);
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel top = new JPanel(new GridLayout(4,2,6,6));
        JTextField tfHoTen = new JTextField();
        JTextField tfDiaChi = new JTextField();
        String[] loais = {"mua lẻ","mua tập thể","mua qua mạng"};
        JComboBox<String> cbLoai = new JComboBox<>(loais);
        JButton btnAdd = new JButton("Thêm");

        top.add(new JLabel("Họ tên:")); top.add(tfHoTen);
        top.add(new JLabel("Địa chỉ:")); top.add(tfDiaChi);
        top.add(new JLabel("Loại:")); top.add(cbLoai);
        top.add(new JLabel("")); top.add(btnAdd);

        JList<NguoiMua> list = new JList<>(listModel);
        JScrollPane sp = new JScrollPane(list);

        btnAdd.addActionListener(e -> {
            try {
                controller.addNguoiMua(tfHoTen.getText(), tfDiaChi.getText(), (String)cbLoai.getSelectedItem());
                refreshList();
                // notify main frame to update its displayed list
                mainFrame.refreshNguoiList();
                JOptionPane.showMessageDialog(this, "Thêm thành công");
                tfHoTen.setText(""); tfDiaChi.setText("");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi lưu file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
        refreshList();
    }

    private void refreshList() {
        listModel.clear();
        controller.listAll().forEach(listModel::addElement);
    }
}