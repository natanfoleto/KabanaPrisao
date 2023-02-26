package com.github.natanfoleto.kabanaprisao.managers;

import com.github.natanfoleto.kabanaprisao.database.repositories.PrisonerLogRepository;
import com.github.natanfoleto.kabanaprisao.entities.PrisonerLog;
import com.github.natanfoleto.kabanaprisao.storages.PrisonersLogStorage;

import java.util.Set;

public class PrisonerLogManager {
    public static void createPrisonersLogStorage() {
        Set<PrisonerLog> prisonersLog = PrisonerLogRepository.getAllPrisonersLog();

        for (PrisonerLog prisonerLog : prisonersLog)
            PrisonersLogStorage.setPrisonerLog(prisonerLog.getName(), prisonerLog);
    }
}
