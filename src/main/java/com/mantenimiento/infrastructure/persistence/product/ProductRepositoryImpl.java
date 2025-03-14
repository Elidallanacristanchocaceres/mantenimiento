package com.mantenimiento.infrastructure.persistence.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mantenimiento.domain.entity.Product;
import com.mantenimiento.domain.repository.ProductRepository;
import com.mantenimiento.infrastructure.database.ConnectionDb;

public class ProductRepositoryImpl implements ProductRepository {
    private final ConnectionDb connection;
    public ProductRepositoryImpl(ConnectionDb connection) {
        this.connection = connection;
    }
    @Override
    public void guardar(Product product) {
        String sql = "INSERT INTO equipos (id, tipoequipo, marca, modelo, serie, descripcion, fechaingreso, fechaentrega) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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
        } catch (SQLException e) {
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
            if (rs.next()) {
                return new Product(rs.getInt("id"), rs.getString("TipoEquipo"), rs.getString("marca"), rs.getString("modelo"), rs.getString("serie"), rs.getString("descripcion"), rs.getString("fechaIngreso"), rs.getString("fechaEntrega"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> listarTodos() {
        String sql = "SELECT * FROM equipos";
        List<Product> products = new ArrayList<>();
        try (Connection conexion = connection.getConexion();
                Statement stmt = conexion.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(new Product(rs.getInt("id"), rs.getString("TipoEquipo"), rs.getString("marca"), rs.getString("modelo"), rs.getString("serie"), rs.getString("descripcion"), rs.getString("fechaIngreso"), rs.getString("fechaEntrega")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public void actualizar(Product product) {
    String sql = "UPDATE equipos SET tipoequipo = ?, marca = ?, modelo = ?, serie = ?, descripcion = ?, fechaingreso = ?, fechaentrega = ? WHERE id = ?";
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
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    @Override
    public void eliminar(int id) {
    String sql = "DELETE FROM equipos WHERE id = ?";
    try (Connection conexion = connection.getConexion();
        PreparedStatement stmt = conexion.prepareStatement(sql)) {
        stmt.setInt(1, id);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}