package com.mantenimiento.application.usecase.client;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mantenimiento.domain.entity.Client;
import com.mantenimiento.domain.repository.ClientRespository;
import com.mantenimiento.infrastructure.util.JsonConverter;

public class ClientUseCase {
    private static ClientRespository repository;
    private final JsonConverter jsonConverter;
        
    public ClientUseCase(ClientRespository repository, JsonConverter jsonConverter) {
        ClientUseCase.repository = repository;
        this.jsonConverter = jsonConverter;
    }

    public String registrarCliente(int id, String nombre, String email, int telefono, String direccion) {
        Client cliente = new Client(id, nombre, email, telefono, direccion);
        repository.guardar(cliente);
        // Convertir a JSON y retornar
        return jsonConverter.toJson(cliente);
    }

    public String obtenerClienteJson(int id) {
        return Optional.ofNullable(repository.buscarPorId(id))
                .map(jsonConverter::toJson)
                .orElse("{\"error\": \"Cliente no encontrado\"}");
    }

    public static Client obtenerCliente(int id) {
        return Optional.ofNullable(repository.buscarPorId(id))
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
    }

    public List<Client> listarClientes() {
        return repository.listarTodos();
    }
    
    // Retorna JSON de todos los clientes
    public String listarClientesJson() {
        return jsonConverter.toJson(
            repository.listarTodos().stream()
                .collect(Collectors.toList())
        );
    }
    
    // Método para filtrar clientes según un predicado
    public List<Client> filtrarClientes(Predicate<Client> filtro) {
        return repository.listarTodos().stream()
                .filter(filtro)
                .collect(Collectors.toList());
    }
    
    // Método para transformar clientes a un mapa por ID
    public Map<Integer, Client> obtenerMapaPorId() {
        return repository.listarTodos().stream()
                .collect(Collectors.toMap(
                    Client::getId,
                    Function.identity()
                ));
    }
    
    // Método para transformar clientes a un mapa por email
    public Map<String, Client> obtenerMapaPorEmail() {
        return repository.listarTodos().stream()
                .collect(Collectors.toMap(
                    Client::getEmail,
                    client -> client,
                    (existing, replacement) -> existing // En caso de duplicados, mantener el existente
                ));
    }
    
    // Método para agrupar clientes por dirección
    public Map<String, List<Client>> agruparPorDireccion() {
        return repository.listarTodos().stream()
                .collect(Collectors.groupingBy(Client::getDireccion));
    }

    public String actualizarCliente(int id, String nombre, String nuevoEmail, int telefono, String direccion) {
        Client cliente = new Client(id, nombre, nuevoEmail, telefono, direccion);
        repository.actualizar(cliente);
        return jsonConverter.toJson(cliente);
    }

    public String eliminarCliente(int id) {
        // Obtener cliente antes de eliminar para retornar su JSON
        Client cliente = repository.buscarPorId(id);
        repository.eliminar(id);
        return Optional.ofNullable(cliente)
                .map(c -> jsonConverter.toJson(Map.of(
                    "mensaje", "Cliente eliminado correctamente",
                    "cliente", c
                )))
                .orElse("{\"error\": \"Cliente no encontrado\"}");
    }

    public boolean actualizarClientePorId(int idActualizar) {
        return Optional.ofNullable(repository.buscarPorId(idActualizar))
                .map(cliente -> {
                    repository.actualizar(cliente);
                    return true;
                })
                .orElse(false);
    }
    
    // Método para procesar todos los clientes con una función
    public <R> List<R> procesarClientes(Function<Client, R> processor) {
        return repository.listarTodos().stream()
                .map(processor)
                .collect(Collectors.toList());
    }
    
    // Método para obtener estadísticas de clientes
    public Map<String, Object> obtenerEstadisticas() {
        List<Client> clientes = repository.listarTodos();
        
        long totalClientes = clientes.size();
        double promedioTelefono = clientes.stream()
                .mapToInt(Client::getTelefono)
                .average()
                .orElse(0);
                
        Map<String, Long> clientesPorDireccion = clientes.stream()
                .collect(Collectors.groupingBy(
                    Client::getDireccion,
                    Collectors.counting()
                ));
                
        return Map.of(
            "totalClientes", totalClientes,
            "promedioTelefono", promedioTelefono,
            "clientesPorDireccion", clientesPorDireccion
        );
    }
}