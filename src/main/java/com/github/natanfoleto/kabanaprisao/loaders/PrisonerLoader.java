package com.github.natanfoleto.kabanaprisao.loaders;

import com.github.natanfoleto.kabanaprisao.managers.PrisonerManager;

public class PrisonerLoader {
    public static void run() {
        PrisonerManager.createPrisonersStorage();
    }
}
