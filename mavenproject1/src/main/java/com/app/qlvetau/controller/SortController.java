package com.app.qlvetau.controller;

import com.app.qlvetau.dao.HoaDonDAO;
import com.app.qlvetau.dao.NguoiMuaDAO;
import com.app.qlvetau.model.entity.HoaDon;
import com.app.qlvetau.model.entity.NguoiMua;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortController {
    private final HoaDonDAO hdDao;
    private final NguoiMuaDAO nmDao;

    public SortController(HoaDonDAO hdDao, NguoiMuaDAO nmDao) {
        this.hdDao = hdDao;
        this.nmDao = nmDao;
    }

    public List<HoaDon> sortByNguoiMuaName() {
        List<HoaDon> all = hdDao.getAll();
        return all.stream()
                .sorted(Comparator.comparing(hd -> {
                    NguoiMua nm = nmDao.getAll().stream().filter(x -> x.getId() == hd.getNguoiMuaId()).findFirst().orElse(null);
                    return nm == null ? "" : nm.getHoTen();
                }))
                .collect(Collectors.toList());
    }

    public List<HoaDon> sortBySoLuongDesc() {
        List<HoaDon> all = hdDao.getAll();
        return all.stream()
                .sorted((a, b) -> Integer.compare(b.getTotalQuantity(), a.getTotalQuantity()))
                .collect(Collectors.toList());
    }
}