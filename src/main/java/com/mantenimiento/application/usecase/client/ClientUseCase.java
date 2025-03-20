package com.mantenimiento.application.usecase.client;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.mantenimiento.domain.entity.Client;
import com.mantenimiento.domain.repository.ClientRespository;

public class ClientUseCase {
    private final ClientRespository repository;
        
    public ClientUseCase(ClientRespository repository) {
        this.repository = repository;
    }

    public void registrarCliente(int id, String nombre, String email, int telefono, String direccion) {
        Client cliente = new Client(id, nombre, email, telefono, direccion);
        repository.guardar(cliente);
    }

    public Client obtenerCliente(int id) {
        return Optional.ofNullable(repository.buscarPorId(id))
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
    }

    public List<Client> listarClientes() {
        return repository.listarTodos();
    }
    
    public List<Client> filtrarClientes(Predicate<Client> filtro) {
        return repository.listarTodos().stream()
                .filter(filtro)
                .collect(Collectors.toList());
    }
    
    public Map<Integer, Client> obtenerMapaPorId() {
        return repository.listarTodos().stream()
                .collect(Collectors.toMap(Client::getId, Function.identity()));
    }
    
    public Map<String, Client> obtenerMapaPorEmail() {
        return repository.listarTodos().stream()
                .collect(Collectors.toMap(Client::getEmail, client -> client, (existing, replacement) -> existing));
    }
    
    public Map<String, List<Client>> agruparPorDireccion() {
        return repository.listarTodos().stream()
                .collect(Collectors.groupingBy(Client::getDireccion));
    }

    public void actualizarCliente(int id, String nombre, String nuevoEmail, int telefono, String direccion) {
        Client cliente = new Client(id, nombre, nuevoEmail, telefono, direccion);
        repository.actualizar(cliente);
    }

    public void eliminarCliente(int id) {
        repository.eliminar(id);
    }
}