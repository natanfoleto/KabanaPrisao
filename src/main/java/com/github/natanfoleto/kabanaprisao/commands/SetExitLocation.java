package com.github.natanfoleto.kabanaprisao.commands;

import com.github.natanfoleto.kabanaprisao.managers.PrisionManager;
import com.github.natanfoleto.kabanaprisao.storages.PrisionStorage;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.entity.Player;

import static com.github.natanfoleto.kabanaprisao.loaders.SettingsLoader.getMessages;

public class SetExitLocation {
    @Command(
            name = "setexit",
            aliases = {"setsaida", "addsaida"},
            permission = "kabanaprisao.setexit",
            target = CommandTarget.PLAYER
    )

    public void onSetPrision(Context<Player> context) {
        Player p = context.getSender();

        PrisionStorage.setExitLocation(p.getLocation());
        PrisionManager.createExitLocationYaml(p.getLocation());

        p.sendMessage(getMessages().getString("Prisao.LocalSaida"));
    }
}
