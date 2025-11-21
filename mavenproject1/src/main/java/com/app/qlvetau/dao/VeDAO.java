package com.app.qlvetau.dao;

import com.app.qlvetau.model.entity.Ve;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class VeDAO {
    private final String filePath = "data/VE.DAT";
    private List<Ve> ds;

    public VeDAO() {
        ds = FileUtil.readFromFile(filePath);
    }

    public List<Ve> getAll() {
        ds = FileUtil.readFromFile(filePath);
        return ds;
    }

    public void add(Ve v) throws IOException {
        ds = FileUtil.readFromFile(filePath);
        int next = ds.stream().map(Ve::getId).max(Comparator.naturalOrder()).orElse(9999) + 1;
        if (next < 10000) next = 10000;
        v.setId(next);
        ds.add(v);
        FileUtil.writeToFile(filePath, ds);
    }

    public Ve findById(int id) {
        ds = FileUtil.readFromFile(filePath);
        Optional<Ve> opt = ds.stream().filter(x -> x.getId() == id).findFirst();
        return opt.orElse(null);
    }
}