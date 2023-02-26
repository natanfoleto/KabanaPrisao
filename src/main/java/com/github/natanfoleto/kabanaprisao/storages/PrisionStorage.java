package com.github.natanfoleto.kabanaprisao.storages;

import com.github.natanfoleto.kabanaprisao.entities.Prision;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class PrisionStorage {
    private static final Map<String, Prision> prisions = new HashMap<>();

    private static Location exitLocation = null;

    public static void setPrision(String name, Prision prision) { prisions.put(name, prision); }

    public static void delPrision(String name) { prisions.remove(name); }

    public static Map<String, Prision> getPrisions() { return prisions; }

    public static void setExitLocation(Location location) { exitLocation = location; }

    public static Location getExitLocation() { return exitLocation; }
}
