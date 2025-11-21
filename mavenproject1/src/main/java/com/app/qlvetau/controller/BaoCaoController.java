package com.app.qlvetau.controller;

import com.app.qlvetau.dao.HoaDonDAO;
import com.app.qlvetau.dao.NguoiMuaDAO;
import com.app.qlvetau.dao.VeDAO;
import com.app.qlvetau.model.business.HoaDonCalculator;
import com.app.qlvetau.model.entity.HoaDon;
import com.app.qlvetau.model.entity.NguoiMua;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaoCaoController {
    private final HoaDonDAO hdDao;
    private final NguoiMuaDAO nmDao;
    private final HoaDonCalculator calc;

    public BaoCaoController(HoaDonDAO hdDao, NguoiMuaDAO nmDao, VeDAO veDao) {
        this.hdDao = hdDao;
        this.nmDao = nmDao;
        this.calc = new HoaDonCalculator(veDao);
    }

    public Map<Integer, Double> tongTienTheoNguoi() {
        Map<Integer, Double> map = new HashMap<>();
        List<HoaDon> list = hdDao.getAll();
        for (HoaDon hd : list) {
            double t = calc.calculateTotal(hd);
            map.put(hd.getNguoiMuaId(), map.getOrDefault(hd.getNguoiMuaId(), 0.0) + t);
        }
        return map;
    }

    public Map<NguoiMua, Double> bangKeTheoNguoi() {
        Map<Integer, Double> raw = tongTienTheoNguoi();
        Map<NguoiMua, Double> result = new HashMap<>();
        for (var e : raw.entrySet()) {
            NguoiMua nm = nmDao.getAll().stream().filter(x -> x.getId() == e.getKey()).findFirst().orElse(null);
            if (nm != null) result.put(nm, e.getValue());
        }
        return result;
    }
}