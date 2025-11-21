package com.app.qlvetau.view;

import com.app.qlvetau.controller.VeController;
import com.app.qlvetau.model.entity.Ve;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class FormVe extends JDialog {
    private final VeController controller;
    private final MainFrame mainFrame;

    private final DefaultTableModel tableModel;
    private final JTable table;

    public FormVe(MainFrame owner, VeController controller) {
        super(owner, "Quản lý Vé", true);
        this.mainFrame = owner;
        this.controller = controller;

        tableModel = new DefaultTableModel(new Object[]{"Mã", "Loại ghế", "Đơn giá"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        initUI();
        setSize(700, 420);
        setLocationRelativeTo(owner);
        refreshTable();
    }

    private void initUI() {
        setLayout(new BorderLayout(8,8));

        // Top: form nhập
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thêm Vé"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblLoai = new JLabel("Loại ghế:");
        JTextField tfLoai = new JTextField();
        JLabel lblGia = new JLabel("Đơn giá:");
        JTextField tfGia = new JTextField();
        JButton btnAdd = new JButton("Thêm");

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2; formPanel.add(lblLoai, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.8; formPanel.add(tfLoai, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2; formPanel.add(lblGia, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.8; formPanel.add(tfGia, gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.8; gbc.anchor = GridBagConstraints.LINE_END; formPanel.add(btnAdd, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Center: danh sách bảng
        JPanel listPanel = new JPanel(new BorderLayout(6,6));
        listPanel.setBorder(BorderFactory.createTitledBorder("Danh sách Vé"));
        listPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Làm mới");
        toolbar.add(btnRefresh);
        listPanel.add(toolbar, BorderLayout.NORTH);

        add(listPanel, BorderLayout.CENTER);

        // Actions
        btnAdd.addActionListener(e -> {
            try {
                double gia = Double.parseDouble(tfGia.getText().trim());
                controller.addVe(tfLoai.getText(), gia);
                refreshTable();
                if (mainFrame != null) mainFrame.refreshVeTable();
                JOptionPane.showMessageDialog(this, "Thêm vé thành công");
                tfLoai.setText(""); tfGia.setText("");
                tfLoai.requestFocus();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Đơn giá phải là số", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi lưu file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRefresh.addActionListener(e -> refreshTable());
    }

    private void refreshTable() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            List<Ve> list = controller.listAll();
            for (Ve v : list) {
                tableModel.addRow(new Object[]{
                        String.format("%05d", v.getId()),
                        v.getLoaiGhe(),
                        String.format("%.2f", v.getDonGia())
                });
            }
        });
    }
}