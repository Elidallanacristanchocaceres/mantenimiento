package com.mantenimiento.infrastructure.persistence.client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import com.mantenimiento.domain.entity.Client;
import com.mantenimiento.domain.repository.ClientRespository;
import com.mantenimiento.infrastructure.database.ConnectionDb;

public class ClientRepositoryImpl implements ClientRespository {
    private final ConnectionDb connection;
    
    public ClientRepositoryImpl(ConnectionDb connection) {
        this.connection = connection;
    }
    @Override
    public void guardar(Client cliente) {
        String sql = "INSERT INTO Clientes (id, nombre, email, telefono, direccion) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = connection.getConexion();
            PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, cliente.getId());
            stmt.setString(2, cliente.getName());
            stmt.setString(3, cliente.getEmail());
            stmt.setInt(4, cliente.getTelefono());
            stmt.setString(5, cliente.getDireccion());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Client buscarPorId(int id) {
        String sql = "SELECT * FROM Clientes WHERE id = ?";
        try (Connection conexion = connection.getConexion();
            PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Client(rs.getInt("id"), rs.getString("nombre"), rs.getString("email"), rs.getInt("telefono"), rs.getString("direccion"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Client> listarTodos() {
        List<Client> client = new ArrayList<>();
        String sql = "SELECT * FROM Clientes";
        try (Connection conexion = connection.getConexion();
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                client.add(new Client(rs.getInt("id"), rs.getString("nombre"), rs.getString("email"), rs.getInt("telefono"), rs.getString("direccion")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public void actualizar(Client cliente) {
        String sql = "UPDATE Clientes SET  nombre = ?, email = ?, telefono = ?, direccion = ? WHERE id = ?";
        try (Connection conexion = connection.getConexion();
            PreparedStatement stmt = conexion.prepareStatement(sql)) {
                stmt.setString(1, cliente.getName());
                stmt.setString(2, cliente.getEmail());
                stmt.setInt(3, cliente.getTelefono());
                stmt.setString(4, cliente.getDireccion());
                stmt.setInt(5, cliente.getId());
                stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM Clientes WHERE id = ?";
        try (Connection conexion = connection.getConexion();
            PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}