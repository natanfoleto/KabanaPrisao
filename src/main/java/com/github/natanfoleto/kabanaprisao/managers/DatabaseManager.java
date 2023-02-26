package com.github.natanfoleto.kabanaprisao.managers;

import com.github.natanfoleto.kabanaprisao.entities.Database;
import com.github.natanfoleto.kabanaprisao.storages.DatabaseStorage;

import static com.github.natanfoleto.kabanaprisao.loaders.SettingsLoader.*;

public class DatabaseManager {
    public static void createDatabase() {
        String databaseType = getConfig().getString("Database.Type");

        Database database = new Database(databaseType);

        if (databaseType.equals("MYSQL")) {
            String host = getConfig().getString("Database.Host");
            String user = getConfig().getString("Database.User");
            String password = getConfig().getString("Database.Password");
            String databaseName = getConfig().getString("Database.Database");

            String url = String.format("jdbc:mysql://%s/%s", host, databaseName);

            database.setHost(host);
            database.setUser(user);
            database.setPassword(password);
            database.setDatabaseName(databaseName);
            database.setUrl(url);
        }

        DatabaseStorage.setDatabase(database);
    }
}
