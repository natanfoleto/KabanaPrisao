package com.github.natanfoleto.kabanaprisao.database.adapters;

import com.github.natanfoleto.kabanaprisao.entities.Prision;
import com.github.natanfoleto.kabanaprisao.entities.Prisoner;
import com.github.natanfoleto.kabanaprisao.storages.PrisionStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrisonerAdapter {
    public static Prisoner adapter(ResultSet rs) throws SQLException {
        final String name = rs.getString("nome");
        final String prisionName = rs.getString("prisao");
        final int iconSlot = rs.getInt("icon_slot");
        final int prisionTime = rs.getInt("prision_time");
        final int timeLeft = rs.getInt("time_left");

        Player player = Bukkit.getPlayer(name);
        Prision prision = PrisionStorage.getPrisions().get(prisionName);

        Prisoner prisoner = new Prisoner(
                name,
                player,
                prision,
                iconSlot,
                prisionTime
        );

        prisoner.setTimeLeft(timeLeft);

        return prisoner;
    }
}
