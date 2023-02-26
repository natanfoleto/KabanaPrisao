package com.github.natanfoleto.kabanaprisao.commands;

import com.github.natanfoleto.kabanaprisao.utils.InventoryUtils;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Prisions {
    @Command(
            name = "prisions",
            aliases = {"prisoes", "presos"},
            permission = "kabanaprisao.prisions",
            target = CommandTarget.PLAYER
    )

    public void onPrisions(Context<Player> context) {
        Player p = context.getSender();

        Inventory inv = InventoryUtils.createMainMenu();

        p.openInventory(inv);
    }
}
