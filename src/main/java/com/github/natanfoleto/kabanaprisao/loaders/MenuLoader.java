package com.github.natanfoleto.kabanaprisao.loaders;

import com.github.natanfoleto.kabanaprisao.helpers.YamlHelper;
import org.bukkit.configuration.file.FileConfiguration;

public class MenuLoader {
    static YamlHelper principal;
    static YamlHelper prisoes;
    static YamlHelper presos;
    static YamlHelper prisaoinfo;

    public static void run() {
        principal = new YamlHelper("menus", "principal", false);
        prisoes = new YamlHelper("menus", "prisoes", false);
        presos = new YamlHelper("menus", "presos", false);
        prisaoinfo = new YamlHelper("menus", "prisao-info", false);
    }

    public static  FileConfiguration getPrincipal() { return principal.getFile(); }
    public static  FileConfiguration getPrisoes() { return prisoes.getFile(); }
    public static  FileConfiguration getPresos() { return presos.getFile(); }
    public static  FileConfiguration getPrisaoInfo() { return prisaoinfo.getFile(); }
}
