package com.github.natanfoleto.kabanaprisao.commands;

import com.github.natanfoleto.kabanaprisao.entities.Prision;
import com.github.natanfoleto.kabanaprisao.storages.PrisionStorage;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.entity.Player;

import static com.github.natanfoleto.kabanaprisao.loaders.SettingsLoader.*;

public class GoPrision {
    @Command(
            name = "prision",
            aliases = {"prisao", "goprisao", "irprisao"},
            permission = "kabanaprisao.prision",
            target = CommandTarget.PLAYER,
            usage = "prisao <nome>"
    )

    public void onGoPrision(
            Context<Player> context,
            String name
    ) {
        Player p = context.getSender();
        Prision prision = PrisionStorage.getPrisions().get(name);

        if (prision == null) {
            p.sendMessage(getMessages().getString("Prisao.NaoExiste"));

            return;
        }

        p.teleport(prision.getLocation());

        p.sendMessage(getMessages().getString("Prisao.Teleportado"));
    }
}
