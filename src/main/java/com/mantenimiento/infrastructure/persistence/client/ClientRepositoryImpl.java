package com.mantenimiento.infrastructure.persistence.client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.mantenimiento.domain.entity.Client;
import com.mantenimiento.domain.repository.ClientRespository;
import com.mantenimiento.infrastructure.database.ConnectionDb;
import com.mantenimiento.infrastructure.util.JsonConverter;

public class ClientRepositoryImpl implements ClientRespository {
    private final ConnectionDb connection;
    private final JsonConverter jsonConverter;
    
    public ClientRepositoryImpl(ConnectionDb connection, JsonConverter jsonConverter) {
        this.connection = connection;
        this.jsonConverter = jsonConverter;
    }
    
    // Función para mapear un ResultSet a un Cliente
    private final Function<ResultSet, Client> clientMapper = rs -> {
        try {
            return new Client(
                rs.getInt("id"), 
                rs.getString("nombre"), 
                rs.getString("email"), 
                rs.getInt("telefono"), 
                rs.getString("direccion")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error al mapear cliente", e);
        }
    };
    
    // Función para mapear un ResultSet a un Map
    private final Function<ResultSet, Map<String, Object>> clientMapMapper = rs -> {
        try {
            return Map.of(
                "id", rs.getInt("id"),
                "nombre", rs.getString("nombre"),
                "email", rs.getString("email"),
                "telefono", rs.getInt("telefono"),
                "direccion", rs.getString("direccion")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error al mapear cliente a mapa", e);
        }
    };
    
    @Override
    public void guardar(Client cliente) {
        String sql = "INSERT INTO Clientes (id, nombre, email, telefono, direccion) VALUES (?, ?, ?, ?, ?)";
        
        // Generar JSON para logging
        String clienteJson = jsonConverter.toJson(cliente);
        System.out.println("Guardando cliente: " + clienteJson);
        
        try (Connection conexion = connection.getConexion();
            PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, cliente.getId());
            stmt.setString(2, cliente.getName());
            stmt.setString(3, cliente.getEmail());
            stmt.setInt(4, cliente.getTelefono());
            stmt.setString(5, cliente.getDireccion());
            stmt.executeUpdate();
            
            System.out.println("Cliente guardado exitosamente: " + clienteJson);
        } catch (SQLException e) {
            System.err.println("Error al guardar cliente: " + clienteJson);
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
            
            Client cliente = rs.next() ? clientMapper.apply(rs) : null;
            
            // Si se encontró el cliente, convertir a JSON para logging
            if (cliente != null) {
                System.out.println("Cliente encontrado: " + jsonConverter.toJson(cliente));
            } else {
                System.out.println("Cliente no encontrado con ID: " + id);
            }
            
            return cliente;
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente con ID: " + id);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Client> listarTodos() {
        String sql = "SELECT * FROM Clientes";
        try (Connection conexion = connection.getConexion();
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            // Usar Stream API para mapear ResultSet a lista de clientes
            List<Client> clients = new ArrayList<>();
            while (rs.next()) {
                clients.add(clientMapper.apply(rs));
            }
            
            // Convertir lista a JSON para logging
            System.out.println("Clientes encontrados: " + jsonConverter.toJson(clients));
            
            return clients;
        } catch (SQLException e) {
            System.err.println("Error al listar clientes");
            e.printStackTrace();
            return List.of(); // Retornar lista vacía en caso de error
        }
    }
    
    // Método adicional para obtener clientes como mapas
    public List<Map<String, Object>> listarTodosComoMapas() {
        String sql = "SELECT * FROM Clientes";
        try (Connection conexion = connection.getConexion();
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            // Usar Stream API para mapear ResultSet a lista de mapas
            List<Map<String, Object>> clientMaps = new ArrayList<>();
            while (rs.next()) {
                clientMaps.add(clientMapMapper.apply(rs));
            }
            
            return clientMaps;
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of(); // Retornar lista vacía en caso de error
        }
    }

    @Override
    public void actualizar(Client cliente) {
        String sql = "UPDATE Clientes SET nombre = ?, email = ?, telefono = ?, direccion = ? WHERE id = ?";
        
        // Generar JSON para logging
        String clienteJson = jsonConverter.toJson(cliente);
        System.out.println("Actualizando cliente: " + clienteJson);
        
        try (Connection conexion = connection.getConexion();
            PreparedStatement stmt = conexion.prepareStatement(sql)) {
                stmt.setString(1, cliente.getName());
                stmt.setString(2, cliente.getEmail());
                stmt.setInt(3, cliente.getTelefono());
                stmt.setString(4, cliente.getDireccion());
                stmt.setInt(5, cliente.getId());
                int filasAfectadas = stmt.executeUpdate();
                
                System.out.println("Cliente actualizado: " + clienteJson + ", filas afectadas: " + filasAfectadas);
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + clienteJson);
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM Clientes WHERE id = ?";
        System.out.println("Eliminando cliente con ID: " + id);
        
        try (Connection conexion = connection.getConexion();
            PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            
            System.out.println("Cliente eliminado con ID: " + id + ", filas afectadas: " + filasAfectadas);
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente con ID: " + id);
            e.printStackTrace();
        }
    }
}