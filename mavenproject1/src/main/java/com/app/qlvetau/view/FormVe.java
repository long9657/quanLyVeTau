package com.app.qlvetau.view;

import com.app.qlvetau.controller.VeController;
import com.app.qlvetau.model.entity.Ve;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class FormVe extends JDialog {
    private final VeController controller;
    private final MainFrame mainFrame;
    private final DefaultListModel<Ve> listModel = new DefaultListModel<>();

    public FormVe(MainFrame owner, VeController controller) {
        super(owner, "Quản lý Vé", true);
        this.mainFrame = owner;
        this.controller = controller;
        initUI();
        setSize(600, 420);
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel top = new JPanel(new GridLayout(3,2,6,6));
        JTextField tfLoai = new JTextField();
        JTextField tfGia = new JTextField();
        JButton btnAdd = new JButton("Thêm");

        top.add(new JLabel("Loại ghế:")); top.add(tfLoai);
        top.add(new JLabel("Đơn giá:")); top.add(tfGia);
        top.add(new JLabel("")); top.add(btnAdd);

        JList<Ve> list = new JList<>(listModel);
        JScrollPane sp = new JScrollPane(list);

        btnAdd.addActionListener(e -> {
            try {
                double gia = Double.parseDouble(tfGia.getText().trim());
                controller.addVe(tfLoai.getText(), gia);
                refreshList();
                // notify main frame to update its displayed list
                mainFrame.refreshVeList();
                JOptionPane.showMessageDialog(this, "Thêm vé thành công");
                tfLoai.setText(""); tfGia.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Đơn giá phải là số", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi lưu file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        setLayout(new BorderLayout(6,6));
        add(top, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
        refreshList();
    }

    private void refreshList() {
        listModel.clear();
        controller.listAll().forEach(listModel::addElement);
    }
}