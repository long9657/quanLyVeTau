package com.app.qlvetau.model.interfaces;

public interface IFileEntity {
    String serialize();
    void deserialize(String data);
}