package com.app.qlvetau.model.entity;

import com.app.qlvetau.model.interfaces.IAutoId;
import com.app.qlvetau.model.interfaces.IFileEntity;

import java.util.Objects;

public class NguoiMua implements IAutoId, IFileEntity {
    private static final long serialVersionUID = 1L;
    private int id; // 5-digit
    private String hoTen;
    private String diaChi;
    private String loai; // mua lẻ, mua tập thể, mua qua mạng

    public NguoiMua() {}

    public NguoiMua(int id, String hoTen, String diaChi, String loai) {
        this.id = id;
        this.hoTen = hoTen;
        this.diaChi = diaChi;
        this.loai = loai;
    }

    @Override
    public int getId() { return id; }

    @Override
    public void setId(int id) { this.id = id; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }

    @Override
    public String toString() {
        return String.format("[%05d] %s - %s - %s", id, hoTen, diaChi, loai);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NguoiMua)) return false;
        NguoiMua that = (NguoiMua) o;
        return id == that.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}