package com.github.natanfoleto.kabanaprisao.database;

import com.github.natanfoleto.kabanaprisao.entities.Database;
import com.github.natanfoleto.kabanaprisao.storages.DatabaseStorage;

import java.sql.*;

public class DatabaseFactory {
    public static Connection createConnection() {
        Database database = DatabaseStorage.getDatabase();

        if ("MYSQL".equals(database.getType())) {
            return buildMYSQL(database);
        }
        throw new UnsupportedOperationException("Database type unsupported!");
    }

    private static Connection buildMYSQL(Database database) {
        try {
            return DriverManager.getConnection(
                    database.getUrl(),
                    database.getUser(),
                    database.getPassword()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection(Connection conn, PreparedStatement stmt) {
        closeConnection(conn);

        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection(Connection conn, PreparedStatement stmt, ResultSet rs) {
        closeConnection(conn, stmt);

        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
