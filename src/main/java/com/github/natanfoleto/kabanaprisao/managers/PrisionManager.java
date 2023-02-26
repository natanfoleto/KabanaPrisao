package com.github.natanfoleto.kabanaprisao.managers;

import com.github.natanfoleto.kabanaprisao.KabanaPrisao;
import com.github.natanfoleto.kabanaprisao.entities.Prision;
import com.github.natanfoleto.kabanaprisao.helpers.YamlHelper;
import com.github.natanfoleto.kabanaprisao.storages.PrisionStorage;
import com.github.natanfoleto.kabanaprisao.utils.ItemUtils;
import com.github.natanfoleto.kabanaprisao.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.natanfoleto.kabanaprisao.loaders.MenuLoader.*;

public class PrisionManager {
    public static void createPrisionsStorage() {
        Bukkit.getConsoleSender().sendMessage("§f[KabanaPrisao] Carregando prisões.");

        File[] directoryListing =
                new File(KabanaPrisao.getInstance().getDataFolder() + "/prisoes").listFiles();

        if (directoryListing != null) {
            for (File child : directoryListing) {
                YamlHelper prisionFile = new YamlHelper(
                        "prisoes",
                        child.getName().replace(".yml", ""),
                        false
                );

                String name = prisionFile.getFile().getString("Name");
                Location location = LocationUtils.deserialize(prisionFile.getFile().getString("Location"));

                List<Integer> slots = new ArrayList<>();
                AtomicInteger iconSlot = new AtomicInteger(0);

                for (Map.Entry<String, Prision> pair : PrisionStorage.getPrisions().entrySet())
                    slots.add(pair.getValue().getIconSlot());

                Collections.sort(slots);

                for (Integer slot : slots) {
                    if (slot == iconSlot.intValue())
                        iconSlot.getAndIncrement();
                    else
                        break;
                }

                Prision prision = new Prision(name, iconSlot.intValue(), location);

                PrisionStorage.setPrision(name, prision);
            }

            Bukkit.getConsoleSender().sendMessage(
                    "§f[KabanaPrisao] Foram carregadas {total} cela(s)."
                            .replace("{total}", String.valueOf(directoryListing.length))
            );

        } else
            Bukkit.getConsoleSender().sendMessage("§f[KabanaPrisao] §cNenhuma prisão encontrada.");
    }

    public static void createPrisionYaml(Prision prision) {
        KabanaPrisao main = KabanaPrisao.getInstance();

        File file = new File(main.getDataFolder(), "prisoes/" + prision.getName() + ".yml");

        if (!file.exists()) {
            YamlHelper prisoes = new YamlHelper("prisoes", prision.getName(), !file.exists());

            int iconId = getPrisoes().getInt("Itens.Id");
            int iconData = getPrisoes().getInt("Itens.Data");
            String iconName = getPrisoes().getString("Itens.Name").replace("{name}", prision.getName());
            List<String> lore = getPrisoes().getStringList("Itens.Lore");
            String serializeLoc = LocationUtils.serialize(prision.getLocation());

            prisoes.setPropertyFile("Name", prision.getName());
            prisoes.setPropertyFile("DisplayName", iconName);
            prisoes.setPropertyFile("Lore", lore);
            prisoes.setPropertyFile("Id", iconId);
            prisoes.setPropertyFile("Data", iconData);
            prisoes.setPropertyFile("Location", serializeLoc);
            prisoes.saveFile();
        }
    }

    public static void deletePrisionYaml(String name) throws IOException {
        KabanaPrisao main = KabanaPrisao.getInstance();

        File file = new File(main.getDataFolder(), "prisoes/" + name + ".yml");

        if (file.exists()) cleanUp(Paths.get(file.getPath()));
    }

    public static void cleanUp(Path path) throws IOException { Files.delete(path); }

    public static void createExitLocationStorage() {
        Bukkit.getConsoleSender().sendMessage("§f[KabanaPrisao] Carregando local de saída.");

        KabanaPrisao main = KabanaPrisao.getInstance();

        File file = new File(main.getDataFolder(), "local-saida.yml");

        if (!file.exists()) {
            Bukkit.getConsoleSender().sendMessage("§f[KabanaPrisao] §cNenhuma local de saída foi setado ainda.");
            return;
        }

        YamlHelper exitLocation = new YamlHelper(null, "local-saida", false);

        Location location = LocationUtils.deserialize(exitLocation.getFile().getString("Location"));

        PrisionStorage.setExitLocation(location);

        Bukkit.getConsoleSender().sendMessage("§f[KabanaPrisao] Local de saída carregado com sucesso.");
    }

    public static void createExitLocationYaml(Location location) {
        KabanaPrisao main = KabanaPrisao.getInstance();

        File file = new File(main.getDataFolder(), "local-saida.yml");

        YamlHelper exitLocation = new YamlHelper(null, "local-saida", !file.exists());

        String serializeLoc = LocationUtils.serialize(location);

        exitLocation.setPropertyFile("Location", serializeLoc);
        exitLocation.saveFile();
    }
}
