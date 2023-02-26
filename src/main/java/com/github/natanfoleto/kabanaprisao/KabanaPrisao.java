package com.github.natanfoleto.kabanaprisao;

import com.github.natanfoleto.kabanaprisao.commands.*;
import com.github.natanfoleto.kabanaprisao.listeners.InventoryListener;
import com.github.natanfoleto.kabanaprisao.listeners.PlayerListener;
import com.github.natanfoleto.kabanaprisao.loaders.*;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static com.github.natanfoleto.kabanaprisao.loaders.SettingsLoader.*;

public final class KabanaPrisao extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§f[KabanaPrisao] Carregando KabanaPrisao v1.0");

        runLoaders();
        registerListeners();
        registerCommands();

        Bukkit.getConsoleSender().sendMessage("§f[KabanaPrisao] Carregado com sucesso.");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§f[KabanaPrisao] Encerado.");
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
    }

    public void registerCommands() {
        BukkitFrame commandFrame = new BukkitFrame(this);

        commandFrame.getMessageHolder().setMessage(
                MessageType.NO_PERMISSION,
                getMessages().getString("Outras.SemPermissao")
        );

        commandFrame.registerCommands(
                new Arrest(),
                new Release(),
                new SetExitLocation(),
                new DelPrision(),
                new GoPrision(),
                new Prisions(),
                new SetPrision(),
                new PrisionInfo(),
                new Reload()
        );
    }

    public void runLoaders() {
        SettingsLoader.run();
        MenuLoader.run();
        PrisionLoader.run();
        DatabaseLoader.run();
        PrisonerLoader.run();
    }

    public static KabanaPrisao getInstance() { return getPlugin(KabanaPrisao.class); }
}
