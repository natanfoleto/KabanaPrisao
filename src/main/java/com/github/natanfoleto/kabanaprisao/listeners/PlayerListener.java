package com.github.natanfoleto.kabanaprisao.listeners;

import com.github.natanfoleto.kabanaprisao.database.repositories.PrisonerLogRepository;
import com.github.natanfoleto.kabanaprisao.entities.PrisonerLog;
import com.github.natanfoleto.kabanaprisao.storages.PrisonersLogStorage;
import com.github.natanfoleto.kabanaprisao.storages.PrisonersStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import static com.github.natanfoleto.kabanaprisao.loaders.SettingsLoader.*;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (PrisonersStorage.getPrisoners().containsKey(player.getName())) {
            if (getConfig().getBoolean("AlertaPresoEntrou"))
                Bukkit.broadcastMessage(
                        getMessages().getString("Preso.PresoEntrou")
                                .replace("{name}", player.getName())
                );

            player.teleport(
                    PrisonersStorage.getPrisoners().get(player.getName())
                            .getPrision().getLocation()
            );
        }

        if (PrisonerLogRepository.getCountUserByName(player.getName()) == 0) {
            PrisonerLogRepository.createPrisonerLog(player.getName());
            PrisonersLogStorage.setPrisonerLog(
                    player.getName(),
                    new PrisonerLog(player.getName(), 0)
            );
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (PrisonersStorage.getPrisoners().containsKey(player.getName())) {
            if (getConfig().getBoolean("AlertaPresoSaiu"))
                Bukkit.broadcastMessage(
                        getMessages().getString("Preso.PresoSaiu")
                                .replace("{name}", player.getName())
                );
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission(getConfig().getString("PermissaoIgnorarRegraComandos")))
            return;

        if (PrisonersStorage.getPrisoners().containsKey(player.getName())) {
            if (getConfig().getBoolean("BloquearTodosComandos")) {
                player.sendMessage(getMessages().getString("Outras.ComandosBloqueados"));
                event.setCancelled(true);

                return;
            }

            String commandSended = event.getMessage().split(" ")[0];

            if (getConfig().getString("PermitirOuNegar").equalsIgnoreCase("permitir")) {
                if (!getConfig().getStringList("ComandosPermitidos").contains(commandSended)) {
                    player.sendMessage(
                            getMessages().getString("Outras.ComandoBloqueado")
                                    .replace("{command}", commandSended)
                    );

                    event.setCancelled(true);
                }
            } else if (getConfig().getString("PermitirOuNegar").equalsIgnoreCase("negar")) {
                if (getConfig().getStringList("ComandosBloqueados").contains(commandSended)) {
                    player.sendMessage(
                            getMessages().getString("Outras.ComandoBloqueado")
                                    .replace("{command}", commandSended)
                    );

                    event.setCancelled(true);
                }
            }
        }
    }
}
