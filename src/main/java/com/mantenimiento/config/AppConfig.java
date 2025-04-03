package com.gestioncitas.application.config;

import com.gestioncitas.domain.repository.*;
import com.gestioncitas.infrastructure.persistence.impl.*;
import com.gestioncitas.infrastructure.ui.console.MenuPrincipal;
import java.sql.Connection;

public class AppConfig {
    private final Connection connection;
    
    public AppConfig(Connection connection) {
        this.connection = connection;
    }
    
    public MenuPrincipal menuPrincipal() {
        // Inicializar repositorios
        CitaRepository citaRepo = new CitaRepositoryImpl(connection);
        MedicoRepository medicoRepo = new MedicoRepositoryImpl(connection);
        PacienteRepository pacienteRepo = new PacienteRepositoryImpl(connection);
        EspecialidadRepository especialidadRepo = new EspecialidadRepositoryImpl(connection);
        
        // Configurar casos de uso
        // (Aquí deberías inicializar todos los casos de uso necesarios)
        
        return new MenuPrincipal(/* Inyectar dependencias necesarias */);
    }
}