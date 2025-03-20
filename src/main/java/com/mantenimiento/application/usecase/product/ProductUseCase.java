package com.mantenimiento.application.usecase.product;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.mantenimiento.domain.entity.Product;
import com.mantenimiento.domain.repository.ProductRepository;

public class ProductUseCase {
    private final ProductRepository repository;

    public ProductUseCase(ProductRepository repository) {
        this.repository = repository;
    }

    public void registrarProducto(int id, String tipoEquipo, String marca, String modelo, String serie, String descripcion, String fechaIngreso, String fechaEntrega) {
        Product producto = new Product(id, tipoEquipo, marca, modelo, serie, descripcion, fechaIngreso, fechaEntrega);
        repository.guardar(producto);
    }

    public Product obtenerProducto(int id) {
        return Optional.ofNullable(repository.buscarPorId(id))
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    public List<Product> listarProductos() {
        return repository.listarTodos();
    }
    
    public List<Product> filtrarProductos(Predicate<Product> filtro) {
        return repository.listarTodos().stream()
                .filter(filtro)
                .collect(Collectors.toList());
    }
    
    public Map<Integer, Product> obtenerMapaPorId() {
        return repository.listarTodos().stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }
    
    public Map<String, List<Product>> agruparPorMarca() {
        return repository.listarTodos().stream()
                .collect(Collectors.groupingBy(Product::getMarca));
    }
    
    public Map<String, Long> contarPorTipoEquipo() {
        return repository.listarTodos().stream()
                .collect(Collectors.groupingBy(Product::getTipoEquipo, Collectors.counting()));
    }

    public void actualizarProducto(int id, String tipoEquipo, String marca, String modelo, String serie, String descripcion, String fechaIngreso, String fechaEntrega) {
        Product producto = new Product(id, tipoEquipo, marca, modelo, serie, descripcion, fechaIngreso, fechaEntrega);
        repository.actualizar(producto);
    }

    public void eliminarProducto(int id) {
        repository.eliminar(id);
    }
}