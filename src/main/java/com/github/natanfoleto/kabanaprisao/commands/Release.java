package com.github.natanfoleto.kabanaprisao.commands;

import com.github.natanfoleto.kabanaprisao.database.repositories.PrisonerRepository;
import com.github.natanfoleto.kabanaprisao.storages.PrisionStorage;
import com.github.natanfoleto.kabanaprisao.storages.PrisonersStorage;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.github.natanfoleto.kabanaprisao.loaders.SettingsLoader.*;

public class Release {
    @Command(
            name = "release",
            aliases = {"soltar"},
            permission = "kabanaprisao.release",
            usage = "soltar <jogador>"
    )

    public void onRelease(
            Context<CommandSender> context,
            String targetName
    ) {
        CommandSender sender = context.getSender();

        if (PrisionStorage.getExitLocation() == null) {
            sender.sendMessage(getMessages().getString("Prisao.NenhumLocalSaida"));
            return;
        }

        if (!PrisonersStorage.getPrisoners().containsKey(targetName)) {
            sender.sendMessage(getMessages().getString("Prisao.JogadorJaSolto"));
            return;
        }

        PrisonersStorage.delPrisoner(targetName);
        PrisonerRepository.deletePrisoner(targetName);

        Player target = Bukkit.getPlayer(targetName);

        if (target != null) {
            target.teleport(PrisionStorage.getExitLocation());
            target.sendMessage(getMessages().getString("Prisao.FoiSoltoPelaStaff"));
            target.playSound(target.getLocation(), Sound.PORTAL_TRAVEL, 1, 2f);
        }

        sender.sendMessage(getMessages().getString("Prisao.Soltou"));

        if (getConfig().getBoolean("AlertaJogadorSolto"))
            for (String item : getMessages().getStringList("Prisao.AlertaSolto"))
                Bukkit.broadcastMessage(item.replace("{name}", targetName));
    }
}
