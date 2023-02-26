package com.github.natanfoleto.kabanaprisao.storages;

import com.github.natanfoleto.kabanaprisao.entities.Database;

public class DatabaseStorage {
    private static Database database;

    public static void setDatabase(Database database) { DatabaseStorage.database = database; }
    public static Database getDatabase() { return database; }
}
