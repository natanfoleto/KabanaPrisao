package com.github.natanfoleto.kabanaprisao.storages;

import com.github.natanfoleto.kabanaprisao.entities.PrisonerLog;

import java.util.HashMap;
import java.util.Map;

public class PrisonersLogStorage {
    private static final Map<String, PrisonerLog> prisonersLog = new HashMap<>();

    public static void setPrisonerLog(String name, PrisonerLog prisonerLog) { prisonersLog.put(name, prisonerLog); }

    public static void incrementTimesArrested(String name) {
        prisonersLog.get(name).incrementTimesArrested();
    }

    public static void delPrisonerLog(String name) { prisonersLog.remove(name); }

    public static Map<String, PrisonerLog> getPrisonersLog() { return prisonersLog; }
}
