package com.app.qlvetau.controller;

import com.app.qlvetau.dao.NguoiMuaDAO;
import com.app.qlvetau.model.entity.NguoiMua;

import java.io.IOException;
import java.util.List;

public class NguoiMuaController {
    private final NguoiMuaDAO dao;

    public NguoiMuaController() { dao = new NguoiMuaDAO(); }

    public List<NguoiMua> listAll() { return dao.getAll(); }

    public void addNguoiMua(String hoTen, String diaChi, String loai) throws IOException {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống");
        }
        if (loai == null || loai.trim().isEmpty()) {
            throw new IllegalArgumentException("Loại không hợp lệ");
        }
        NguoiMua nm = new NguoiMua(0, hoTen.trim(), diaChi == null ? "" : diaChi.trim(), loai.trim());
        dao.add(nm);
    }

    public NguoiMuaDAO getDao() { return dao; }
}