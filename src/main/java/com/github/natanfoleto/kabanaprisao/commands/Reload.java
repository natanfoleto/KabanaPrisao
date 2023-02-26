package com.github.natanfoleto.kabanaprisao.commands;

import com.github.natanfoleto.kabanaprisao.loaders.MenuLoader;
import com.github.natanfoleto.kabanaprisao.loaders.PrisionLoader;
import com.github.natanfoleto.kabanaprisao.loaders.SettingsLoader;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.entity.Player;

import static com.github.natanfoleto.kabanaprisao.loaders.SettingsLoader.*;

public class Reload {
    @Command(
            name = "kabanaprisao",
            aliases = {"prisaoreload", "reloadprisao"},
            permission = "kabanaprisao.reload"
    )

    public void onReload(Context<Player> context) {
        context.sendMessage(getMessages().getString("Plugin.Recarregar"));

        SettingsLoader.run();
        MenuLoader.run();
        PrisionLoader.run();

        context.sendMessage(getMessages().getString("Plugin.Recarregado"));
    }
}
