package com.github.natanfoleto.kabanaprisao.loaders;

import com.github.natanfoleto.kabanaprisao.managers.PrisionManager;

public class PrisionLoader {
    public static void run() {
        PrisionManager.createPrisionsStorage();
        PrisionManager.createExitLocationStorage();
    }
}
