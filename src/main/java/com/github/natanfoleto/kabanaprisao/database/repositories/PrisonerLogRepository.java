package com.github.natanfoleto.kabanaprisao.database.repositories;

import com.github.natanfoleto.kabanaprisao.database.DatabaseFactory;
import com.github.natanfoleto.kabanaprisao.database.adapters.PrisonerLogAdapter;
import com.github.natanfoleto.kabanaprisao.entities.PrisonerLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class PrisonerLogRepository {
    public static void createTablePrisonersLog() {
        Connection conn = DatabaseFactory.createConnection();

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS presos_log(" +
                    "`nome` varchar(50) PRIMARY KEY NOT NULL, " +
                    "`times_arrested` int NOT NULL DEFAULT 1" +
                    ")"
            );

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseFactory.closeConnection(conn, stmt);
        }
    }

    public static Set<PrisonerLog> getAllPrisonersLog() {
        Connection conn = DatabaseFactory.createConnection();

        PreparedStatement stmt = null;
        ResultSet rs = null;

        Set<PrisonerLog> prisonersLog = new HashSet<>();

        try {
            stmt = conn.prepareStatement("SELECT * FROM presos_log");

            rs = stmt.executeQuery();

            while (rs.next()) prisonersLog.add(PrisonerLogAdapter.adapter(rs));

            return prisonersLog;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }  finally {
            DatabaseFactory.closeConnection(conn, stmt, rs);
        }
    }

    public static Integer getCountUserByName(String name) {
        Connection conn = DatabaseFactory.createConnection();

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Integer count = null;

        try {
            stmt = conn.prepareStatement("SELECT COUNT(*) FROM presos_log WHERE `nome` = ?;");

            stmt.setString(1, name);

            rs = stmt.executeQuery();

            if (rs.next()) count = rs.getInt(1);

            return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }  finally {
            DatabaseFactory.closeConnection(conn, stmt, rs);
        }
    }

    public static PrisonerLog getPrisonerLogByName(String name) {
        Connection conn = DatabaseFactory.createConnection();

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement("SELECT * FROM presos_log WHERE `nome` = ?");

            stmt.setString(1, name);

            rs = stmt.executeQuery();

           return PrisonerLogAdapter.adapter(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }  finally {
            DatabaseFactory.closeConnection(conn, stmt, rs);
        }
    }

    public static void createPrisonerLog(String name) {
        Connection conn = DatabaseFactory.createConnection();

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement("INSERT INTO presos_log (`nome`) VALUES(?)");

            stmt.setString(1, name);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }  finally {
            DatabaseFactory.closeConnection(conn, stmt);
        }
    }

    public static void deletePrisonerLog(String name) {
        Connection conn = DatabaseFactory.createConnection();

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement("DELETE FROM presos_log WHERE `nome` = ?");

            stmt.setString(1, name);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }  finally {
            DatabaseFactory.closeConnection(conn, stmt);
        }
    }

    public static void updateTimes(String nome) {
        Connection conn = DatabaseFactory.createConnection();

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement("UPDATE presos_log SET " +
                    "`times_arrested` = `times_arrested` + 1 " +
                    "WHERE `nome` = ?"
            );

            stmt.setString(1, nome);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }  finally {
            DatabaseFactory.closeConnection(conn, stmt);
        }
    }
}
