package com.github.natanfoleto.kabanaprisao.loaders;

import com.github.natanfoleto.kabanaprisao.managers.PrisonerLogManager;

public class PrisonerLogLoader {
    public static void run() {
        PrisonerLogManager.createPrisonersLogStorage();
    }
}
