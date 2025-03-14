package com.mantenimiento.domain.repository;

import java.util.List;

import com.mantenimiento.domain.entity.Product;


public interface ProductRepository {
    void guardar(Product product);
    Product buscarPorId(int id);
    List<Product> listarTodos();
    void actualizar(Product product);
    void eliminar(int id);
}