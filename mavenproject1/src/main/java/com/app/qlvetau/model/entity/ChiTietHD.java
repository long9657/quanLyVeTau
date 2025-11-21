package com.app.qlvetau.model.entity;

import com.app.qlvetau.model.interfaces.IFileEntity;

import java.util.Objects;

public class ChiTietHD implements IFileEntity {
    private static final long serialVersionUID = 1L;
    private int veId;
    private int soLuong;

    public ChiTietHD() {}

    public ChiTietHD(int veId, int soLuong) {
        this.veId = veId;
        this.soLuong = soLuong;
    }

    public int getVeId() { return veId; }
    public void setVeId(int veId) { this.veId = veId; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    @Override
    public String toString() {
        return String.format("Ve[%05d] x %d", veId, soLuong);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChiTietHD)) return false;
        ChiTietHD that = (ChiTietHD) o;
        return veId == that.veId && soLuong == that.soLuong;
    }

    @Override
    public int hashCode() { return Objects.hash(veId, soLuong); }
}