package com.app.qlvetau.model.business;

import com.app.qlvetau.dao.VeDAO;
import com.app.qlvetau.model.entity.ChiTietHD;
import com.app.qlvetau.model.entity.HoaDon;
import com.app.qlvetau.model.entity.Ve;

public class HoaDonCalculator {
    private final VeDAO veDAO;

    public HoaDonCalculator(VeDAO veDAO) {
        this.veDAO = veDAO;
    }

    public double calculateTotal(HoaDon hd) {
        double total = 0;
        for (ChiTietHD ct : hd.getChiTiets()) {
            Ve v = veDAO.findById(ct.getVeId());
            if (v != null) {
                total += ct.getSoLuong() * v.getDonGia();
            }
        }
        return total;
    }
}