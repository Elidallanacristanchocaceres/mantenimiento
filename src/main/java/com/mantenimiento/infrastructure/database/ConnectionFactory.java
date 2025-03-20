package com.mantenimiento.infrastructure.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionFactory {
    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static Connection conexion;

    public static synchronized Connection crearConexion() {
        if (conexion == null) {
            try {
                // Cambia estas credenciales según tu configuración de MySQL
                String dbUrl = "jdbc:mysql://localhost:3306/mantenimiento";
                String dbUser = "root"; // Usuario de MySQL
                String dbPassword = "campus2023"; // Contraseña de MySQL

                LOGGER.info("Conectando a la base de datos MySQL: " + dbUrl);
                conexion = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                LOGGER.info("Conexión a la base de datos establecida correctamente");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al crear la conexión a la base de datos", e);
                throw new RuntimeException("No se pudo establecer la conexión a la base de datos", e);
            }
        }
        return conexion;
    }

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