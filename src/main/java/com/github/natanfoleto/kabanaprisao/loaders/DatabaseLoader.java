package com.github.natanfoleto.kabanaprisao.loaders;

import com.github.natanfoleto.kabanaprisao.database.repositories.*;
import com.github.natanfoleto.kabanaprisao.managers.DatabaseManager;

public class DatabaseLoader {
    public static void run() {
        DatabaseManager.createDatabase();

        PrisonerRepository.createTablePrisoners();
        PrisonerLogRepository.createTablePrisonersLog();
    }
}
