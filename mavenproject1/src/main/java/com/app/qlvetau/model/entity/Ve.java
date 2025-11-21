package com.app.qlvetau.model.entity;

import com.app.qlvetau.model.interfaces.IAutoId;
import com.app.qlvetau.model.interfaces.IFileEntity;

import java.util.Objects;

public class Ve implements IAutoId, IFileEntity {
    private static final long serialVersionUID = 1L;
    private int id; // 5-digit
    private String loaiGhe;
    private double donGia;

    public Ve() {}

    public Ve(int id, String loaiGhe, double donGia) {
        this.id = id;
        this.loaiGhe = loaiGhe;
        this.donGia = donGia;
    }

    @Override
    public int getId() { return id; }

    @Override
    public void setId(int id) { this.id = id; }

    public String getLoaiGhe() { return loaiGhe; }
    public void setLoaiGhe(String loaiGhe) { this.loaiGhe = loaiGhe; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    @Override
    public String toString() {
        return String.format("[%05d] %s - %.2f", id, loaiGhe, donGia);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ve)) return false;
        Ve ve = (Ve) o;
        return id == ve.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}