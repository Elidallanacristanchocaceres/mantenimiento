package com.mantenimiento.infrastructure.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase encargada de inicializar la base de datos automáticamente
 */
public class DatabaseInitializer {
    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());
    private final Connection connection;

    public DatabaseInitializer(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inicializa la base de datos creando las tablas necesarias si no existen
     */
    public void initializeDatabase() {
        LOGGER.info("Inicializando base de datos...");
        
        // Lista de scripts SQL para crear las tablas
        List<String> createTableScripts = Arrays.asList(
            // Tabla de clientes
            "CREATE TABLE IF NOT EXISTS clientes (" +
            "id INTEGER PRIMARY KEY, " +
            "nombre TEXT NOT NULL, " +
            "email TEXT NOT NULL, " +
            "telefono INTEGER, " +
            "direccion TEXT" +
            ")",
            
            // Tabla de productos
            "CREATE TABLE IF NOT EXISTS productos (" +
            "id INTEGER PRIMARY KEY, " +
            "tipo_equipo TEXT NOT NULL, " +
            "marca TEXT NOT NULL, " +
            "modelo TEXT, " +
            "serie TEXT, " +
            "descripcion TEXT, " +
            "fecha_ingreso TEXT, " +
            "fecha_entrega TEXT" +
            ")"
        );
        
        // Ejecutar cada script SQL
        createTableScripts.forEach(executeSQL("Creando tabla"));
        
        LOGGER.info("Base de datos inicializada correctamente");
    }
    
    /**
     * Crea un Consumer que ejecuta un script SQL y maneja las excepciones
     */
    private Consumer<String> executeSQL(String operationName) {
        return sql -> {
            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
                LOGGER.info(operationName + " ejecutado correctamente");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al ejecutar SQL: " + sql, e);
                throw new RuntimeException("Error al inicializar la base de datos", e);
            }
        };
    }
    
    /**
     * Método para insertar datos de ejemplo (opcional)
     */
    public void insertarDatosEjemplo() {
        LOGGER.info("Insertando datos de ejemplo...");
        
        // Verificar si ya existen datos
        try (Statement statement = connection.createStatement()) {
            // Insertar clientes de ejemplo solo si no existen
            statement.execute(
                "INSERT OR IGNORE INTO clientes (id, nombre, email, telefono, direccion) VALUES " +
                "(1, 'Cliente Ejemplo', 'ejemplo@gmail.com', 123456789, 'Dirección Ejemplo')"
            );
            
            // Insertar productos de ejemplo solo si no existen
            statement.execute(
                "INSERT OR IGNORE INTO productos (id, tipo_equipo, marca, modelo, serie, descripcion, fecha_ingreso, fecha_entrega) VALUES " +
                "(1, 'Laptop', 'Marca Ejemplo', 'Modelo X', 'SER123', 'Laptop para reparación', '01012023', '15012023')"
            );
            
            LOGGER.info("Datos de ejemplo insertados correctamente");
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error al insertar datos de ejemplo", e);
            // No lanzamos excepción aquí para que la aplicación continúe funcionando
        }
    }
}
