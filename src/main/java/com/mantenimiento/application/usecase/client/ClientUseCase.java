package com.mantenimiento.application.usecase.client;

import java.util.List;

import com.mantenimiento.domain.entity.Client;
import com.mantenimiento.domain.repository.ClientRespository;

public class ClientUseCase {
    private static ClientRespository repository;
        
            public ClientUseCase(ClientRespository repository) {
                ClientUseCase.repository = repository;
        }
    
        public void registrarCliente(int id, String nombre, String email, int telefono, String direccion) {
            Client cliente = new Client(id, nombre, email, telefono, direccion);
            repository.guardar(cliente); 
        }
    
        public static Client obtenerCliente(int id) {
            return repository.buscarPorId(id);
    }

    public List<Client> listarClientes() {
        return repository.listarTodos();
    }

    public void actualizarCliente(int id, String nombre, String nuevoEmail, int telefono, String direccion) {
        Client cliente = new Client(id, nombre, nuevoEmail, telefono, direccion);
        repository.actualizar(cliente);
    }

    public void eliminarCliente(int id) {
        repository.eliminar(id);
    }

    public boolean actualizarClientePorId(int idActualizar) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizarClientePorId'");
    }
}