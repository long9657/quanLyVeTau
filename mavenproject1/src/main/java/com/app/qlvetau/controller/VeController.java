package com.app.qlvetau.controller;

import com.app.qlvetau.dao.VeDAO;
import com.app.qlvetau.model.entity.Ve;

import java.io.IOException;
import java.util.List;

public class VeController {
    private final VeDAO dao;

    public VeController() { dao = new VeDAO(); }

    public List<Ve> listAll() { return dao.getAll(); }

    public void addVe(String loaiGhe, double donGia) throws IOException {
        if (loaiGhe == null || loaiGhe.trim().isEmpty()) {
            throw new IllegalArgumentException("Loại ghế không được trống");
        }
        if (donGia <= 0) {
            throw new IllegalArgumentException("Đơn giá phải > 0");
        }
        Ve v = new Ve(0, loaiGhe.trim(), donGia);
        dao.add(v);
    }

    public VeDAO getDao() { return dao; }
}