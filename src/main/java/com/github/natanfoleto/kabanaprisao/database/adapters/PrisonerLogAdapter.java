package com.github.natanfoleto.kabanaprisao.database.adapters;

import com.github.natanfoleto.kabanaprisao.entities.PrisonerLog;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrisonerLogAdapter {
    public static PrisonerLog adapter(ResultSet rs) throws SQLException {
        final String name = rs.getString("nome");
        final int timesArrested = rs.getInt("times_arrested");

        return new PrisonerLog(name, timesArrested);
    }
}
