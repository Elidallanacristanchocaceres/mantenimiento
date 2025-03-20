package com.mantenimiento.infrastructure.persistence.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.mantenimiento.domain.entity.Product;
import com.mantenimiento.domain.repository.ProductRepository;
import com.mantenimiento.infrastructure.database.ConnectionDb;

public class ProductRepositoryImpl implements ProductRepository {
    private final ConnectionDb connection;

    public ProductRepositoryImpl(ConnectionDb connection) {
        this.connection = connection;
    }

    private final Function<ResultSet, Product> productMapper = rs -> {
        try {
            return new Product(
                rs.getInt("id"), 
                rs.getString("tipoEquipo"), 
                rs.getString("marca"), 
                rs.getString("modelo"), 
                rs.getString("serie"), 
                rs.getString("descripcion"), 
                rs.getString("fechaIngreso"), 
                rs.getString("fechaEntrega")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error al mapear producto", e);
        }
    };

    @Override
    public void guardar(Product producto) {
        String sql = "INSERT INTO Equipos (id, tipoEquipo, marca, modelo, serie, descripcion, fechaIngreso, fechaEntrega) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conexion = connection.getConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, producto.getId());
            stmt.setString(2, producto.getTipoEquipo());
            stmt.setString(3, producto.getMarca());
            stmt.setString(4, producto.getModelo());
            stmt.setString(5, producto.getSerie());
            stmt.setString(6, producto.getDescripcion());
            stmt.setString(7, producto.getFechaIngreso());
            stmt.setString(8, producto.getFechaEntrega());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar producto", e);
        }
    }

    @Override
    public Product buscarPorId(int id) {
        String sql = "SELECT * FROM Equipos WHERE id = ?";
        try (Connection conexion = connection.getConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? productMapper.apply(rs) : null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar producto con ID: " + id, e);
        }
    }

    @Override
    public List<Product> listarTodos() {
        String sql = "SELECT * FROM Equipos";
        try (Connection conexion = connection.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(productMapper.apply(rs));
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar productos", e);
        }
    }

    @Override
    public void actualizar(Product producto) {
        String sql = "UPDATE Equipos SET tipoEquipo = ?, marca = ?, modelo = ?, serie = ?, descripcion = ?, fechaIngreso = ?, fechaEntrega = ? WHERE id = ?";
        
        try (Connection conexion = connection.getConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, producto.getTipoEquipo());
            stmt.setString(2, producto.getMarca());
            stmt.setString(3, producto.getModelo());
            stmt.setString(4, producto.getSerie());
            stmt.setString(5, producto.getDescripcion());
            stmt.setString(6, producto.getFechaIngreso());
            stmt.setString(7, producto.getFechaEntrega());
            stmt.setInt(8, producto.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar producto", e);
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM Equipos WHERE id = ?";
        
        try (Connection conexion = connection.getConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto con ID: " + id, e);
        }
    }
}