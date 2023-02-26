package com.github.natanfoleto.kabanaprisao.database.repositories;

import com.github.natanfoleto.kabanaprisao.database.DatabaseFactory;
import com.github.natanfoleto.kabanaprisao.database.adapters.PrisonerAdapter;
import com.github.natanfoleto.kabanaprisao.entities.Prisoner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class PrisonerRepository {
    public static void createTablePrisoners() {
        Connection conn = DatabaseFactory.createConnection();

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS presos(" +
                    "`nome` varchar(50) NOT NULL, " +
                    "`prisao` varchar(50) NOT NULL, " +
                    "`icon_slot` SMALLINT NOT NULL, " +
                    "`prision_time` int NOT NULL, " +
                    "`time_left` int NOT NULL, " +
                    "`reason` VARCHAR(200), " +
                    "`status` SMALLINT DEFAULT 1, " +
                    "PRIMARY KEY (`nome`, `prisao`) " +
                    ")"
            );

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseFactory.closeConnection(conn, stmt);
        }
    }

    public static Set<Prisoner> getAllEnabledPrisoners() {
        Connection conn = DatabaseFactory.createConnection();

        PreparedStatement stmt = null;
        ResultSet rs = null;

        Set<Prisoner> prisoners = new HashSet<>();

        try {
            stmt = conn.prepareStatement("SELECT * FROM presos WHERE `status` = 1");

            rs = stmt.executeQuery();

            while (rs.next()) prisoners.add(PrisonerAdapter.adapter(rs));

            return prisoners;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }  finally {
            DatabaseFactory.closeConnection(conn, stmt, rs);
        }
    }

    public static void createPrisoner(Prisoner prisoner) {
        Connection conn = DatabaseFactory.createConnection();

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement("REPLACE INTO presos" +
                    "(`nome`, `prisao`, `icon_slot`, `prision_time`, `time_left`, `reason`) " +
                    "VALUES(?, ?, ?, ?, ?, ?)"
            );

            stmt.setString(1, prisoner.getPlayer().getName());
            stmt.setString(2, prisoner.getPrision().getName());
            stmt.setInt(3, prisoner.getIconSlot());
            stmt.setInt(4, prisoner.getPrisionTime());
            stmt.setInt(5, prisoner.getPrisionTime());
            stmt.setString(6, prisoner.getReason());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }  finally {
            DatabaseFactory.closeConnection(conn, stmt);
        }
    }

    public static void updateStatus(String name) {
        Connection conn = DatabaseFactory.createConnection();

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement("UPDATE presos SET " +
                    "`status` = 0 " +
                    "WHERE `nome` = ?"
            );

            stmt.setString(1, name);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }  finally {
            DatabaseFactory.closeConnection(conn, stmt);
        }
    }

    public static void updateTimeLeft(String nome, Integer time) {
        Connection conn = DatabaseFactory.createConnection();

        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement("UPDATE presos SET " +
                    "`time_left` = ? " +
                    "WHERE `nome` = ?"
            );

            stmt.setInt(1, time);
            stmt.setString(2, nome);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }  finally {
            DatabaseFactory.closeConnection(conn, stmt);
        }
    }
}
