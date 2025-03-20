package com.mantenimiento.infrastructure.database;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionDbImpl implements ConnectionDb {
    private final Connection connection;

    public ConnectionDbImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConexion() throws SQLException {
        return connection;
    }
}