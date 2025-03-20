package com.mantenimiento.application.usecase.product;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.mantenimiento.domain.entity.Product;
import com.mantenimiento.domain.repository.ProductRepository;
import com.mantenimiento.infrastructure.util.JsonConverter;

public class ProductUseCase {
    private final ProductRepository repository;
    private final JsonConverter jsonConverter;

    public ProductUseCase(ProductRepository repository, JsonConverter jsonConverter) {
        this.repository = repository;
        this.jsonConverter = jsonConverter;
    }

    public String registrarproducto(int id, String tipoEquipo, String marca, String modelo, String serie, String descripcion, String fechaIngreso, String fechaEntrega) {
        Product producto = new Product(id, tipoEquipo, marca, modelo, serie, descripcion, fechaIngreso, fechaEntrega);
        repository.guardar(producto);
        return jsonConverter.toJson(producto);
    }

    public String obtenerProductoJson(int id) {
        return Optional.ofNullable(repository.buscarPorId(id))
                .map(jsonConverter::toJson)
                .orElse("{\"error\": \"Producto no encontrado\"}");
    }

    public Product obtenerproducto(int id) {
        return Optional.ofNullable(repository.buscarPorId(id))
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    public List<Product> listarproductos() {
        return repository.listarTodos();
    }
    
    public String listarProductosJson() {
        return jsonConverter.toJson(
            repository.listarTodos().stream()
                .collect(Collectors.toList())
        );
    }
    
    // Método para filtrar productos por marca
    public List<Product> filtrarPorMarca(String marca) {
        return repository.listarTodos().stream()
                .filter(p -> p.getMarca().equalsIgnoreCase(marca))
                .collect(Collectors.toList());
    }
    
    // Método para filtrar productos según un predicado personalizable
    public List<Product> filtrarProductos(Predicate<Product> filtro) {
        return repository.listarTodos().stream()
                .filter(filtro)
                .collect(Collectors.toList());
    }
    
    // Método para transformar productos a un mapa por ID
    public Map<Integer, Product> obtenerMapaPorId() {
        return repository.listarTodos().stream()
                .collect(Collectors.toMap(
                    Product::getId,
                    Function.identity()
                ));
    }
    
    // Método para agrupar productos por marca
    public Map<String, List<Product>> agruparPorMarca() {
        return repository.listarTodos().stream()
                .collect(Collectors.groupingBy(Product::getMarca));
    }
    
    // Método para contar productos por tipo de equipo
    public Map<String, Long> contarPorTipoEquipo() {
        return repository.listarTodos().stream()
                .collect(Collectors.groupingBy(
                    Product::getTipoEquipo,
                    Collectors.counting()
                ));
    }

    public String actualizarproducto(int id, String tipoEquipo, String marca, String modelo, String serie, String descripcion, String fechaIngreso, String fechaEntrega) {
        Product producto = new Product(id, tipoEquipo, marca, modelo, serie, descripcion, fechaIngreso, fechaEntrega);
        repository.actualizar(producto);
        return jsonConverter.toJson(producto);
    }

    public String eliminarproducto(int id) {
        // Obtener producto antes de eliminar para retornar su JSON
        Product producto = repository.buscarPorId(id);
        repository.eliminar(id);
        return Optional.ofNullable(producto)
                .map(p -> jsonConverter.toJson(Map.of(
                    "mensaje", "Producto eliminado correctamente",
                    "producto", p
                )))
                .orElse("{\"error\": \"Producto no encontrado\"}");
    }
    
    // Método para procesar todos los productos con una función
    public <R> List<R> procesarProductos(Function<Product, R> processor) {
        return repository.listarTodos().stream()
                .map(processor)
                .collect(Collectors.toList());
    }
    
    // Método para obtener estadísticas de productos
    public Map<String, Object> obtenerEstadisticas() {
        List<Product> productos = repository.listarTodos();
        
        // Usando lambdas para calcular estadísticas
        long totalProductos = productos.size();
        Map<String, Long> productosPorMarca = productos.stream()
                .collect(Collectors.groupingBy(
                    Product::getMarca,
                    Collectors.counting()
                ));
                
        Map<String, List<String>> modelosPorMarca = productos.stream()
                .collect(Collectors.groupingBy(
                    Product::getMarca,
                    Collectors.mapping(Product::getModelo, Collectors.toList())
                ));
                
        return Map.of(
            "totalProductos", totalProductos,
            "productosPorMarca", productosPorMarca,
            "modelosPorMarca", modelosPorMarca
        );
    }
}