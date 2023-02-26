package com.github.natanfoleto.kabanaprisao.loaders;

import com.github.natanfoleto.kabanaprisao.helpers.YamlHelper;
import org.bukkit.configuration.file.FileConfiguration;

public class SettingsLoader {
    static YamlHelper config;
    static YamlHelper messages;
    static YamlHelper commands;

    public static void run() {
        config = new YamlHelper(null, "config", false);
        messages = new YamlHelper(null, "messages", false);
        commands = new YamlHelper(null, "comandos", false);
    }

    public static FileConfiguration getConfig() { return config.getFile(); }
    public static  FileConfiguration getMessages() { return messages.getFile(); }
}
