package com.mantenimiento.infrastructure.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mantenimiento.config.HexaSingleton;

/**
 * Fábrica de conexiones a la base de datos
 */
public class ConnectionFactory {
    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static Connection conexion;
    private static boolean databaseInitialized = false;

    /**
     * Crea una conexión a la base de datos
     * @return Conexión a la base de datos
     */
    public static synchronized Connection crearConexion() {
        if (conexion == null) {
            try {
                // Obtener la URL de la base de datos desde la configuración
                String dbUrl = HexaSingleton.INSTANCIA.get("db.url", "jdbc:sqlite:mantenimiento.db");
                
                LOGGER.info("Conectando a la base de datos: " + dbUrl);
                conexion = DriverManager.getConnection(dbUrl);
                
                // Inicializar la base de datos si no se ha hecho antes
                if (!databaseInitialized) {
                    inicializarBaseDatos(conexion);
                    databaseInitialized = true;
                }
                
                LOGGER.info("Conexión a la base de datos establecida correctamente");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al crear la conexión a la base de datos", e);
                throw new RuntimeException("No se pudo establecer la conexión a la base de datos", e);
            }
        }
        return conexion;
    }
    
    /**
     * Inicializa la base de datos creando las tablas necesarias
     * @param conexion Conexión a la base de datos
     */
    private static void inicializarBaseDatos(Connection conexion) {
        DatabaseInitializer initializer = new DatabaseInitializer(conexion);
        initializer.initializeDatabase();
        
        // Opcional: insertar datos de ejemplo
        boolean insertarEjemplos = Boolean.parseBoolean(
            HexaSingleton.INSTANCIA.get("db.insert.examples", "false")
        );
        
        if (insertarEjemplos) {
            initializer.insertarDatosEjemplo();
        }
    }
    
    /**
     * Cierra la conexión a la base de datos
     */
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
                LOGGER.info("Conexión a la base de datos cerrada correctamente");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error al cerrar la conexión a la base de datos", e);
            }
        }
    }
}