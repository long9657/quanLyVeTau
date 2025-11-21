package com.app.qlvetau.dao;

import com.app.qlvetau.model.entity.NguoiMua;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * DAO đọc file mỗi lần getAll() được gọi để đảm bảo dữ liệu luôn đồng bộ với file.
 */
public class NguoiMuaDAO {
    private final String filePath = "data/NGUOIMUA.DAT";
    private List<NguoiMua> ds;

    public NguoiMuaDAO() {
        ds = FileUtil.readFromFile(filePath);
    }

    public List<NguoiMua> getAll() {
        // reload from file each time to reflect external changes / new seed files
        ds = FileUtil.readFromFile(filePath);
        return ds;
    }

    public void add(NguoiMua nm) throws IOException {
        // reload to get current max id
        ds = FileUtil.readFromFile(filePath);
        int next = ds.stream().map(NguoiMua::getId).max(Comparator.naturalOrder()).orElse(9999) + 1;
        if (next < 10000) next = 10000;
        nm.setId(next);
        ds.add(nm);
        FileUtil.writeToFile(filePath, ds);
    }

    public NguoiMua findById(int id) {
        ds = FileUtil.readFromFile(filePath);
        Optional<NguoiMua> opt = ds.stream().filter(x -> x.getId() == id).findFirst();
        return opt.orElse(null);
    }
}