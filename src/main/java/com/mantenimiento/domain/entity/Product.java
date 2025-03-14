package com.mantenimiento.domain.entity;

public class Product {
    private int id;
    private String TipoEquipo;
    private String Marca;
    private String Modelo;
    private String Serie;
    private String Descripcion;
    private String FechaIngreso;
    private String FechaEntrega;
    public Product(int id, String TipoEquipo, String Marca, String Modelo, String Serie, String Descripcion, String FechaIngreso, String FechaEntrega) {
        this.id = id;
        this.TipoEquipo = TipoEquipo;
        this.Marca = Marca;
        this.Modelo = Modelo;
        this.Serie = Serie;
        this.Descripcion = Descripcion;
        this.FechaIngreso = FechaIngreso;
        this.FechaEntrega = FechaEntrega;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTipoEquipo() {
        return TipoEquipo;
    }
    public void setTipoEquipo(String TipoEquipo) {
        this.TipoEquipo = TipoEquipo;
    }
    public String getMarca() {
        return Marca;
    }
    public void setMarca(String Marca) {
        this.Marca = Marca;
    }
    public String getModelo() {
        return Modelo;
    }
    public void setModelo(String Modelo) {
        this.Modelo = Modelo;
    }
    public String getSerie() {
        return Serie;
    }
    public void setSerie(String Serie) {
        this.Serie = Serie;
    }
    public String getDescripcion() {
        return Descripcion;
    }
    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }
    public String getFechaIngreso() {
        return FechaIngreso;
    }
    public void setFechaIngreso(String FechaIngreso) {
        this.FechaIngreso = FechaIngreso;
    }
    public String getFechaEntrega() {
        return FechaEntrega;
    }
    public void setFechaEntrega(String FechaEntrega) {
        this.FechaEntrega = FechaEntrega;
    }
    
}