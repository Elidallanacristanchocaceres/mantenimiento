package com.mantenimiento.infrastructure.util;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
* Utilidad para convertir objetos a JSON y viceversa
*/
public class JsonConverter {
    private final Gson gson;
    
    public JsonConverter() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }
    
    /**
     * Convierte un objeto a su representación JSON
     */
    public String toJson(Object obj) {
        return gson.toJson(obj);
    }
    
    /**
     * Convierte un JSON a un objeto del tipo especificado
     */
    public <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }
    
    /**
     * Convierte un JSON a una lista de objetos del tipo especificado
     */
    public <T> List<T> fromJsonList(String json, Class<T> classOfT) {
        Type listType = TypeToken.getParameterized(List.class, classOfT).getType();
        return gson.fromJson(json, listType);
    }
    
    /**
     * Convierte un JSON a un mapa con claves String
     */
    public <T> Map<String, T> fromJsonMap(String json, Class<T> valueType) {
        Type mapType = TypeToken.getParameterized(Map.class, String.class, valueType).getType();
        return gson.fromJson(json, mapType);
    }
}