package com.app.qlvetau.controller;

import com.app.qlvetau.dao.HoaDonDAO;
import com.app.qlvetau.dao.VeDAO;
import com.app.qlvetau.model.business.HoaDonCalculator;
import com.app.qlvetau.model.entity.ChiTietHD;
import com.app.qlvetau.model.entity.HoaDon;

import java.io.IOException;
import java.util.List;

public class HoaDonController {
    private final HoaDonDAO hdDao;
    private final VeDAO veDao;
    private final HoaDonCalculator calculator;

    public HoaDonController(VeDAO veDao) {
        this.veDao = veDao;
        this.hdDao = new HoaDonDAO();
        this.calculator = new HoaDonCalculator(veDao);
    }

    public List<HoaDon> listAll() { return hdDao.getAll(); }

    public void addHoaDon(HoaDon hd) throws IOException {
        if (hd.getChiTiets() == null || hd.getChiTiets().isEmpty()) {
            throw new IllegalArgumentException("Hóa đơn phải có ít nhất 1 loại vé");
        }
        if (hd.getChiTiets().size() > 4) {
            throw new IllegalArgumentException("Mỗi hóa đơn không quá 4 loại vé");
        }
        for (ChiTietHD ct : hd.getChiTiets()) {
            if (ct.getSoLuong() <= 0 || ct.getSoLuong() > 20) {
                throw new IllegalArgumentException("Số lượng mỗi loại phải trong khoảng 1..20");
            }
            if (veDao.findById(ct.getVeId()) == null) {
                throw new IllegalArgumentException("Không tìm thấy vé id: " + ct.getVeId());
            }
        }
        hdDao.add(hd);
    }

    public double calculateTotal(HoaDon hd) { return calculator.calculateTotal(hd); }

    public HoaDonDAO getDao() { return hdDao; }
}