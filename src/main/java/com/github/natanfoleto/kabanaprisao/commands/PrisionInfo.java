package com.github.natanfoleto.kabanaprisao.commands;

import com.github.natanfoleto.kabanaprisao.utils.InventoryUtils;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PrisionInfo {
    @Command(
            name = "prision",
            aliases = {"prisao", "infoprisao", "prisaoinfo", "prisioninfo", "infoprision"},
            permission = "kabanaprisao.prisioninfo",
            target = CommandTarget.PLAYER
    )

    public void onPrisionInfo(Context<Player> context) {
        Player p = context.getSender();

        Inventory inv = InventoryUtils.createPrisonInfoMenu(p);

        p.openInventory(inv);
    }
}
