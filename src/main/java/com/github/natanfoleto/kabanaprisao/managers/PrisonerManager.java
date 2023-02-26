package com.github.natanfoleto.kabanaprisao.managers;

import com.github.natanfoleto.kabanaprisao.database.repositories.PrisonerRepository;
import com.github.natanfoleto.kabanaprisao.entities.Prisoner;
import com.github.natanfoleto.kabanaprisao.schedulers.PrisonCooldown;
import com.github.natanfoleto.kabanaprisao.storages.PrisonersStorage;
import org.bukkit.Bukkit;

import java.util.Set;

public class PrisonerManager {
    public static void createPrisonersStorage() {
        Bukkit.getConsoleSender().sendMessage("§f[KabanaPrisao] Carregando prisioneiros.");

        Set<Prisoner> prisoners = PrisonerRepository.getAllPrisoners();

        if (prisoners.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage("§f[KabanaPrisao] §cNenhum prisioneiro no servidor.");
            return;
        }

        for (Prisoner prisoner : prisoners) {
            PrisonersStorage.setPrisoner(prisoner.getName(), prisoner);
            PrisonCooldown.create(prisoner.getName(), prisoner.getTimeLeft());
        }

        Bukkit.getConsoleSender().sendMessage(
                "§f[KabanaPrisao] {total} Prisioneiro(s) carregado(s) com sucesso."
                        .replace("{total}", String.valueOf(prisoners.size()))
        );
    }
}
