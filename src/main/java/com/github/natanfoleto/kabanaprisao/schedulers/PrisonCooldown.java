package com.github.natanfoleto.kabanaprisao.schedulers;

import com.github.natanfoleto.kabanaprisao.KabanaPrisao;
import com.github.natanfoleto.kabanaprisao.database.repositories.PrisonerRepository;
import com.github.natanfoleto.kabanaprisao.storages.PrisionStorage;
import com.github.natanfoleto.kabanaprisao.storages.PrisonersStorage;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;

import static com.github.natanfoleto.kabanaprisao.loaders.SettingsLoader.*;

public class PrisonCooldown {
    private static final BukkitScheduler scheduler = Bukkit.getScheduler();

    private static final Map<String, Integer> tasks = new HashMap<>();
    private static final Map<String, Integer> cooldowns = new HashMap<>();

    public static int getCooldown(String key) {
        return Integer.parseInt(cooldowns.get(key).toString());
    }

    public static void create(String name, int time) {
        int taskId = scheduler.scheduleSyncRepeatingTask(KabanaPrisao.getInstance(), new Runnable() {
            int cooldown = time;

            @Override
            public void run() {
                if (cooldown == 0) {
                    scheduler.cancelTask(tasks.get(name));
                    cooldowns.remove(name);

                    PrisonersStorage.delPrisoner(name);
                    PrisonerRepository.updateStatus(name);

                    Player target = Bukkit.getPlayer(name);

                    if (target != null) {
                        if (PrisionStorage.getExitLocation() != null)
                            target.teleport(PrisionStorage.getExitLocation());

                        target.sendMessage(getMessages().getString("Preso.FoiSolto"));
                        target.playSound(target.getLocation(), Sound.PORTAL_TRAVEL, 1, 2f);
                    }

                    if (getConfig().getBoolean("AlertaJogadorSolto"))
                        for (String item : getMessages().getStringList("Preso.AlertaSolto"))
                            Bukkit.broadcastMessage(item.replace("{name}", name));

                    tasks.remove(name);
                }

                if (cooldown > 0) {
                    cooldowns.put(name, cooldown);

                    if (getConfig().getBoolean("DiminuirTempoOnline")) {
                        if (Bukkit.getPlayer(name) != null) {
                            cooldown--;
                            cooldowns.put(name, cooldown);

                            PrisonerRepository.updateTimeLeft(name, cooldown);
                        }
                    } else {
                        cooldown--;
                        cooldowns.put(name, cooldown);

                        PrisonerRepository.updateTimeLeft(name, cooldown);
                    }
                }
            }
        }, 0, 1200);

        tasks.put(name, taskId);
    }
}
