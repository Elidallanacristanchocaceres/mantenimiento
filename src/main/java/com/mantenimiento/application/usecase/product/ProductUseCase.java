package com.mantenimiento.application.usecase.product;

import java.util.List;

import com.mantenimiento.domain.entity.Product;
import com.mantenimiento.domain.repository.ProductRepository;



public class ProductUseCase {
    private final ProductRepository repository;

    public ProductUseCase(ProductRepository repository) {
        this.repository = repository;
    }

    public void registrarproducto(int id, String TipoEquipo, String Marca, String Modelo, String Serie, String Descripcion, String FechaIngreso, String FechaEntrega) {
        Product producto = new Product(id, TipoEquipo, Marca, Modelo, Serie, Descripcion, FechaIngreso, FechaEntrega);
        repository.guardar(producto);
    }

    public Product obtenerproducto(int id) {
        return repository.buscarPorId(id);
    }

    public List<Product> listarproductos() {
        return repository.listarTodos();
    }

    public void actualizarproducto(int id, String TipoEquipo, String Marca, String Modelo, String Serie, String Descripcion, String FechaIngreso, String FechaEntrega) {
        Product producto = new Product(id, TipoEquipo, Marca, Modelo, Serie, Descripcion, FechaIngreso, FechaEntrega);
        repository.actualizar(producto);
    }

    public void eliminarproducto(int id) {
        repository.eliminar(id);
    }

    
}