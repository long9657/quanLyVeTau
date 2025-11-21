package com.app.qlvetau.view;

import com.app.qlvetau.controller.NguoiMuaController;
import com.app.qlvetau.model.entity.NguoiMua;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class FormNguoiMua extends JDialog {
    private final NguoiMuaController controller;
    private final MainFrame mainFrame;

    private final DefaultTableModel tableModel;
    private final JTable table;

    public FormNguoiMua(MainFrame owner, NguoiMuaController controller) {
        super(owner, "Quản lý Người Mua", true);
        this.mainFrame = owner;
        this.controller = controller;

        tableModel = new DefaultTableModel(new Object[]{"Mã", "Họ tên", "Địa chỉ", "Loại"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        initUI();
        setSize(700, 450);
        setLocationRelativeTo(owner);
        refreshTable();
    }

    private void initUI() {
        setLayout(new BorderLayout(8,8));

        // Top: form nhập
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thêm Người Mua"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblHoTen = new JLabel("Họ tên:");
        JTextField tfHoTen = new JTextField();
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        JTextField tfDiaChi = new JTextField();
        JLabel lblLoai = new JLabel("Loại:");
        String[] loais = {"mua lẻ","mua tập thể","mua qua mạng"};
        JComboBox<String> cbLoai = new JComboBox<>(loais);
        JButton btnAdd = new JButton("Thêm");

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.15; formPanel.add(lblHoTen, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.85; formPanel.add(tfHoTen, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.15; formPanel.add(lblDiaChi, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.85; formPanel.add(tfDiaChi, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.15; formPanel.add(lblLoai, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.85; formPanel.add(cbLoai, gbc);

        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.85; gbc.anchor = GridBagConstraints.LINE_END; formPanel.add(btnAdd, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Center: danh sách bảng
        JPanel listPanel = new JPanel(new BorderLayout(6,6));
        listPanel.setBorder(BorderFactory.createTitledBorder("Danh sách Người mua"));
        listPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Toolbar nhỏ trên bảng
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Làm mới");
        toolbar.add(btnRefresh);
        listPanel.add(toolbar, BorderLayout.NORTH);

        add(listPanel, BorderLayout.CENTER);

        // Actions
        btnAdd.addActionListener(e -> {
            try {
                controller.addNguoiMua(tfHoTen.getText(), tfDiaChi.getText(), (String)cbLoai.getSelectedItem());
                refreshTable();
                if (mainFrame != null) mainFrame.refreshNguoiTable();
                JOptionPane.showMessageDialog(this, "Thêm thành công");
                tfHoTen.setText(""); tfDiaChi.setText("");
                tfHoTen.requestFocus();
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
            List<NguoiMua> list = controller.listAll();
            for (NguoiMua n : list) {
                tableModel.addRow(new Object[]{
                        String.format("%05d", n.getId()),
                        n.getHoTen(),
                        n.getDiaChi(),
                        n.getLoai()
                });
            }
        });
    }
}