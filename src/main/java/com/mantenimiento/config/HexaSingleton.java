package com.mantenimiento.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Supplier;

public enum HexaSingleton {
    INSTANCIA;
    
    private final Properties propiedades = new Properties();
    
    HexaSingleton() {
        cargarConfiguraciones("config.properties");
    }
    
    private void cargarConfiguraciones(String rutaArchivo) {
        Supplier<InputStream> inputStreamSupplier = () -> 
            getClass().getClassLoader().getResourceAsStream(rutaArchivo);
            
        try (InputStream inputStream = inputStreamSupplier.get()) {
            if (inputStream == null) {
                throw new IOException("Archivo no encontrado: " + rutaArchivo);
            }
            propiedades.load(inputStream);
        } catch (IOException e) {
            System.err.println("❌ Error cargando configuración: " + e.getMessage());
            throw new RuntimeException("No se pudo cargar la configuración", e);
        }
    }
    
    public String get(String clave) {
        return propiedades.getProperty(clave, "No encontrado");
    }
    
    // Método con lambda para obtener propiedades con valor por defecto
    public String get(String clave, String valorPorDefecto) {
        return propiedades.getProperty(clave, valorPorDefecto);
    }
    
    // Método con lambda para obtener propiedades como entero
    public int getAsInt(String clave, int valorPorDefecto) {
        return propiedades.entrySet().stream()
            .filter(entry -> entry.getKey().equals(clave))
            .map(entry -> {
                try {
                    return Integer.parseInt(entry.getValue().toString());
                } catch (NumberFormatException e) {
                    return valorPorDefecto;
                }
            })
            .findFirst()
            .orElse(valorPorDefecto);
    }
}