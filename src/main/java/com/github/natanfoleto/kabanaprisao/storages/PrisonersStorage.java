package com.github.natanfoleto.kabanaprisao.storages;

import com.github.natanfoleto.kabanaprisao.entities.Prisoner;

import java.util.HashMap;
import java.util.Map;

public class PrisonersStorage {
    private static final Map<String, Prisoner> prisoners = new HashMap<>();

    public static void setPrisoner(String name, Prisoner prisoner) { prisoners.put(name, prisoner); }

    public static void delPrisoner(String name) { prisoners.remove(name); }

    public static Map<String, Prisoner> getPrisoners() { return prisoners; }
}
