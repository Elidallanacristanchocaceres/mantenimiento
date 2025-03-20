package com.mantenimiento.infrastructure.persistence.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.mantenimiento.domain.entity.Product;
import com.mantenimiento.domain.repository.ProductRepository;
import com.mantenimiento.infrastructure.database.ConnectionDb;
import com.mantenimiento.infrastructure.util.JsonConverter;

public class ProductRepositoryImpl implements ProductRepository {
    private final ConnectionDb connection;
    private final JsonConverter jsonConverter;
    
    public ProductRepositoryImpl(ConnectionDb connection, JsonConverter jsonConverter) {
        this.connection = connection;
        this.jsonConverter = jsonConverter;
    }
    
    // Función para mapear un ResultSet a un Producto
    private final Function<ResultSet, Product> productMapper = rs -> {
        try {
            return new Product(
                rs.getInt("id"), 
                rs.getString("tipoequipo"), 
                rs.getString("marca"), 
                rs.getString("modelo"), 
                rs.getString("serie"), 
                rs.getString("descripcion"), 
                rs.getString("fechaingreso"), 
                rs.getString("fechaentrega")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error al mapear producto", e);
        }
    };
    
    // Función para mapear un ResultSet a un Map
    private final Function<ResultSet, Map<String, Object>> productMapMapper = rs -> {
        try {
            return Map.of(
                "id", rs.getInt("id"),
                "tipoEquipo", rs.getString("tipoequipo"),
                "marca", rs.getString("marca"),
                "modelo", rs.getString("modelo"),
                "serie", rs.getString("serie"),
                "descripcion", rs.getString("descripcion"),
                "fechaIngreso", rs.getString("fechaingreso"),
                "fechaEntrega", rs.getString("fechaentrega")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error al mapear producto a mapa", e);
        }
    };
    
    @Override
    public void guardar(Product product) {
        String sql = "INSERT INTO equipos (id, tipoequipo, marca, modelo, serie, descripcion, fechaingreso, fechaentrega) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        // Generar JSON para logging
        String productoJson = jsonConverter.toJson(product);
        System.out.println("Guardando producto: " + productoJson);
        
        try (Connection conexion = connection.getConexion();
            PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, product.getId());
            stmt.setString(2, product.getTipoEquipo());
            stmt.setString(3, product.getMarca());
            stmt.setString(4, product.getModelo());
            stmt.setString(5, product.getSerie());
            stmt.setString(6, product.getDescripcion());
            stmt.setString(7, product.getFechaIngreso());
            stmt.setString(8, product.getFechaEntrega());
            stmt.executeUpdate();
            
            System.out.println("Producto guardado exitosamente: " + productoJson);
        } catch (SQLException e) {
            System.err.println("Error al guardar producto: " + productoJson);
            e.printStackTrace();
        }
    }

    @Override
    public Product buscarPorId(int id) {
        String sql = "SELECT * FROM equipos WHERE id = ?";
        try (Connection conexion = connection.getConexion();
                PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            Product producto = rs.next() ? productMapper.apply(rs) : null;
            
            // Si se encontró el producto, convertir a JSON para logging
            if (producto != null) {
                System.out.println("Producto encontrado: " + jsonConverter.toJson(producto));
            } else {
                System.out.println("Producto no encontrado con ID: " + id);
            }
            
            return producto;
        } catch (SQLException e) {
            System.err.println("Error al buscar producto con ID: " + id);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> listarTodos() {
        String sql = "SELECT * FROM equipos";
        try (Connection conexion = connection.getConexion();
                Statement stmt = conexion.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            
            // Usar Stream API para mapear ResultSet a lista de productos
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(productMapper.apply(rs));
            }
            
            // Convertir lista a JSON para logging
            System.out.println("Productos encontrados: " + jsonConverter.toJson(products));
            
            return products;
        } catch (SQLException e) {
            System.err.println("Error al listar productos");
            e.printStackTrace();
            return List.of(); // Retornar lista vacía en caso de error
        }
    }
    
    // Método adicional para obtener productos como mapas
    public List<Map<String, Object>> listarTodosComoMapas() {
        String sql = "SELECT * FROM equipos";
        try (Connection conexion = connection.getConexion();
                Statement stmt = conexion.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            
            // Usar Stream API para mapear ResultSet a lista de mapas
            List<Map<String, Object>> productMaps = new ArrayList<>();
            while (rs.next()) {
                productMaps.add(productMapMapper.apply(rs));
            }
            
            return productMaps;
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of(); // Retornar lista vacía en caso de error
        }
    }

    @Override
    public void actualizar(Product product) {
        String sql = "UPDATE equipos SET tipoequipo = ?, marca = ?, modelo = ?, serie = ?, descripcion = ?, fechaingreso = ?, fechaentrega = ? WHERE id = ?";
        
        // Generar JSON para logging
        String productoJson = jsonConverter.toJson(product);
        System.out.println("Actualizando producto: " + productoJson);
        
        try (Connection conexion = connection.getConexion();
            PreparedStatement stmt = conexion.prepareStatement(sql)) {
                stmt.setString(1, product.getTipoEquipo());
                stmt.setString(2, product.getMarca());
                stmt.setString(3, product.getModelo());
                stmt.setString(4, product.getSerie());
                stmt.setString(5, product.getDescripcion());
                stmt.setString(6, product.getFechaIngreso());
                stmt.setString(7, product.getFechaEntrega());
                stmt.setInt(8, product.getId());
                int filasAfectadas = stmt.executeUpdate();
                
                System.out.println("Producto actualizado: " + productoJson + ", filas afectadas: " + filasAfectadas);
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + productoJson);
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM equipos WHERE id = ?";
        System.out.println("Eliminando producto con ID: " + id);
        
        try (Connection conexion = connection.getConexion();
            PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            
            System.out.println("Producto eliminado con ID: " + id + ", filas afectadas: " + filasAfectadas);
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto con ID: " + id);
            e.printStackTrace();
        }
    }
}