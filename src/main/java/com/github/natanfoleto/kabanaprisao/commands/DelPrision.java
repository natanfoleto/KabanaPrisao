package com.github.natanfoleto.kabanaprisao.commands;

import com.github.natanfoleto.kabanaprisao.managers.PrisionManager;
import com.github.natanfoleto.kabanaprisao.storages.PrisionStorage;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.entity.Player;

import java.io.IOException;

import static com.github.natanfoleto.kabanaprisao.loaders.SettingsLoader.*;

public class DelPrision {
    @Command(
            name = "unsetprision",
            aliases = {"unsetprisao", "delprisao", "deletarprisao"},
            permission = "kabanaprisao.unsetprision",
            usage = "delprisao <nome>"
    )

    public void onDelPrision(
            Context<Player> context,
            String name
    ) throws IOException {
        Player p = context.getSender();

        if (!PrisionStorage.getPrisions().containsKey(name)) {
            p.sendMessage(getMessages().getString("Prisao.NaoExiste"));

            return;
        }

        PrisionStorage.delPrision(name);
        PrisionManager.deletePrisionYaml(name);

        p.sendMessage(getMessages().getString("Prisao.Deletada"));
    }
}
