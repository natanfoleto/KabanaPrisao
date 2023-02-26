package com.github.natanfoleto.kabanaprisao.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemUtils {
    public static ItemStack createItemMenu(
            String name,
            List<String> lore,
            Integer id,
            short data
    ) {
        ItemStack icon =
                new ItemStack(Material.getMaterial(id), 1, data);

        ItemMeta itemMeta = icon.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName(name);
            itemMeta.setLore(lore);
            icon.setItemMeta(itemMeta);
        }

        return icon;
    }
}
