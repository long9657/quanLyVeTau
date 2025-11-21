package com.app.qlvetau.model.entity;

import com.app.qlvetau.model.interfaces.IFileEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HoaDon implements IFileEntity {
    private static final long serialVersionUID = 1L;
    private int hoaDonId;
    private int nguoiMuaId;
    private List<ChiTietHD> chiTiets = new ArrayList<>();

    public HoaDon() {}

    public HoaDon(int hoaDonId, int nguoiMuaId) {
        this.hoaDonId = hoaDonId;
        this.nguoiMuaId = nguoiMuaId;
    }

    public int getHoaDonId() { return hoaDonId; }
    public void setHoaDonId(int hoaDonId) { this.hoaDonId = hoaDonId; }

    public int getNguoiMuaId() { return nguoiMuaId; }
    public void setNguoiMuaId(int nguoiMuaId) { this.nguoiMuaId = nguoiMuaId; }

    public List<ChiTietHD> getChiTiets() { return chiTiets; }
    public void setChiTiets(List<ChiTietHD> chiTiets) { this.chiTiets = chiTiets; }

    public void addChiTiet(ChiTietHD ct) { chiTiets.add(ct); }

    public int getTotalQuantity() {
        return chiTiets.stream().mapToInt(ChiTietHD::getSoLuong).sum();
    }

    @Override
    public String toString() {
        return String.format("HoaDon[%d] - NguoiMua[%05d] - Items:%d", hoaDonId, nguoiMuaId, chiTiets.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HoaDon)) return false;
        HoaDon hoaDon = (HoaDon) o;
        return hoaDonId == hoaDon.hoaDonId;
    }

    @Override
    public int hashCode() { return Objects.hash(hoaDonId); }
}