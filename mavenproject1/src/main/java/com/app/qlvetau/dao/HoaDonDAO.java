package com.app.qlvetau.dao;

import com.app.qlvetau.model.entity.HoaDon;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class HoaDonDAO {
    private final String filePath = "data/HOADON.DAT";
    private List<HoaDon> ds;

    public HoaDonDAO() {
        ds = FileUtil.readFromFile(filePath);
    }

    public List<HoaDon> getAll() {
        ds = FileUtil.readFromFile(filePath);
        return ds;
    }

    public void add(HoaDon hd) throws IOException {
        ds = FileUtil.readFromFile(filePath);
        int next = ds.stream().map(HoaDon::getHoaDonId).max(Comparator.naturalOrder()).orElse(0) + 1;
        hd.setHoaDonId(next);
        ds.add(hd);
        FileUtil.writeToFile(filePath, ds);
    }

    public void saveAll() throws IOException {
        FileUtil.writeToFile(filePath, ds);
    }
}