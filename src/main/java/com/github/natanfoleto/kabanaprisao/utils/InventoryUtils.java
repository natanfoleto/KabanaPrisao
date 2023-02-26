package com.github.natanfoleto.kabanaprisao.utils;

import com.github.natanfoleto.kabanaprisao.entities.Prision;
import com.github.natanfoleto.kabanaprisao.entities.Prisoner;
import com.github.natanfoleto.kabanaprisao.schedulers.PrisonCooldown;
import com.github.natanfoleto.kabanaprisao.storages.PrisionStorage;
import com.github.natanfoleto.kabanaprisao.storages.PrisonersLogStorage;
import com.github.natanfoleto.kabanaprisao.storages.PrisonersStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.github.natanfoleto.kabanaprisao.loaders.MenuLoader.*;
import static com.github.natanfoleto.kabanaprisao.loaders.SettingsLoader.*;

public class InventoryUtils {
    public static Inventory createMainMenu() {
        Inventory inv = Bukkit.createInventory(
                null,
                getPrincipal().getInt("Tamanho"),
                getPrincipal().getString("Nome")
        );

        ItemStack prisions = ItemUtils.createItemMenu(
                getPrincipal().getString("Itens.Prisoes.Name"),
                getPrincipal().getStringList("Itens.Prisoes.Lore"),
                getPrincipal().getInt("Itens.Prisoes.Id"),
                (short) getPrincipal().getInt("Itens.Prisoes.Data")
        );

        ItemStack prisoners = ItemUtils.createItemMenu(
                getPrincipal().getString("Itens.Presos.Name"),
                getPrincipal().getStringList("Itens.Presos.Lore"),
                getPrincipal().getInt("Itens.Presos.Id"),
                (short) getPrincipal().getInt("Itens.Presos.Data")
        );

        inv.setItem(
                getPrincipal().getInt("Itens.Prisoes.Slot"),
                prisions
        );

        inv.setItem(
                getPrincipal().getInt("Itens.Presos.Slot"),
                prisoners
        );

        return inv;
    }

    public static Inventory createPrisonsMenu() {
        Inventory inv =  Bukkit.createInventory(
                null,
                getPrisoes().getInt("Tamanho"),
                getPrisoes().getString("Nome")
        );

        ItemStack back = ItemUtils.createItemMenu(
                getPrisoes().getString("Voltar.Name"),
                getPrisoes().getStringList("Voltar.Lore"),
                getPrisoes().getInt("Voltar.Id"),
                (short) getPrisoes().getInt("Voltar.Data")
        );

        inv.setItem(getPrisoes().getInt("Voltar.Slot"), back);

        for (Prision prision : PrisionStorage.getPrisions().values()) {
            if (
                    prision.getIconSlot() > (getPrisoes().getInt("Tamanho")) - 1 ||
                            prision.getIconSlot() < 0
            )
                continue;

            boolean isBusy = true;

            for (Prisoner prisoner : PrisonersStorage.getPrisoners().values())
                if (prisoner.getPrision() == prision) {
                    isBusy = false;
                    break;
                }

            String isBusyText =
                    isBusy ?
                            getMessages().getString("Prisao.StatusOcupada") :
                            getMessages().getString("Prisao.StatusLivre");

            List<String> lore = new ArrayList<>();

            for (String item : getPrisoes().getStringList("Itens.Lore"))
                lore.add(item.replace("{status}", isBusyText));

            ItemStack prisonIcon = ItemUtils.createItemMenu(
                    getPrisoes().getString("Itens.Name").replace("{name}", prision.getName()),
                    lore,
                    getPrisoes().getInt("Itens.Id"),
                    (short) getPrisoes().getInt("Itens.Data")
            );

            inv.setItem(prision.getIconSlot(), prisonIcon);
        }

        return inv;
    }

    public static Inventory createPrisonersMenu() {
        Inventory inv =  Bukkit.createInventory(
                null,
                getPresos().getInt("Tamanho"),
                getPresos().getString("Nome")
        );

        ItemStack back = ItemUtils.createItemMenu(
                getPresos().getString("Voltar.Name"),
                getPresos().getStringList("Voltar.Lore"),
                getPresos().getInt("Voltar.Id"),
                (short) getPresos().getInt("Voltar.Data")
        );

        inv.setItem(getPresos().getInt("Voltar.Slot"), back);

        for (Prisoner prisoner : PrisonersStorage.getPrisoners().values()) {
            if (
                    prisoner.getIconSlot() > (getPrisoes().getInt("Tamanho")) - 1 ||
                            prisoner.getIconSlot() < 0
            )
                continue;

            int prisonTime = prisoner.getPrisionTime();
            int timeLeft = PrisonCooldown.getCooldown(prisoner.getName());
            int timesArrested = PrisonersLogStorage.getPrisonersLog().get(prisoner.getName()).getTimesArrested();

            List<String> lorePrisionTime = new ArrayList<>();

            for (String item : getPresos().getStringList("Itens.Lore")) {
                lorePrisionTime.add(
                        item
                                .replace("{time}", getPrisonTimeText(prisonTime))
                                .replace("{timeLeft}", getPrisonTimeText(timeLeft))
                                .replace("{amount}", String.valueOf(timesArrested))
                                .replace("{reason}", prisoner.getReason())
                );
            }

            ItemStack prisonerIcon = ItemUtils.createItemMenu(
                    getPresos().getString("Itens.Name").replace("{name}", prisoner.getName()),
                    lorePrisionTime,
                    getPresos().getInt("Itens.Id"),
                    (short) getPresos().getInt("Itens.Data")
            );

            inv.setItem(prisoner.getIconSlot(), prisonerIcon);
        }

        return inv;
    }

    public static Inventory createPrisonInfoMenu(Player player) {
        Inventory inv = Bukkit.createInventory(
                null,
                getPrisaoInfo().getInt("Tamanho"),
                getPrisaoInfo().getString("Nome")
        );

        String isArrested =
                PrisonersStorage.getPrisoners().containsKey(player.getName()) ?
                        getMessages().getString("Preso.StatusPreso") :
                        getMessages().getString("Preso.StatusSolto");

        String reason =
                PrisonersStorage.getPrisoners().containsKey(player.getName()) ?
                        PrisonersStorage.getPrisoners().get(player.getName()).getReason() :
                        "§aNenhum motivo, você está solto.";

        int timesArrested = PrisonersLogStorage.getPrisonersLog().get(player.getName()).getTimesArrested();

        List<String> loreMyInformations = new ArrayList<>();

        for (String item : getPrisaoInfo().getStringList("Itens.MinhasInformacoes.Lore")) {
            loreMyInformations.add(
                    item
                            .replace("{status}", isArrested)
                            .replace("{amount}", String.valueOf(timesArrested))
                            .replace("{reason}", reason)
            );
        }

        ItemStack myInformations = ItemUtils.createItemMenu(
                getPrisaoInfo().getString("Itens.MinhasInformacoes.Name"),
                loreMyInformations,
                getPrisaoInfo().getInt("Itens.MinhasInformacoes.Id"),
                (short) getPrisaoInfo().getInt("Itens.MinhasInformacoes.Data")
        );

        List<String> lorePrisionTime = new ArrayList<>();

        if (PrisonersStorage.getPrisoners().containsKey(player.getName())) {
            Prisoner prisoner = PrisonersStorage.getPrisoners().get(player.getName());

            int prisonTime = prisoner.getPrisionTime();
            int timeLeft = PrisonCooldown.getCooldown(player.getName());

            for (String item : getPrisaoInfo().getStringList("Itens.TempoDePrisao.Lore")) {
                lorePrisionTime.add(
                        item
                                .replace("{time}", getPrisonTimeText(prisonTime))
                                .replace("{timeLeft}", getPrisonTimeText(timeLeft))
                );
            }
        } else lorePrisionTime.add("§7Você não está preso.");

        ItemStack prisionTime = ItemUtils.createItemMenu(
                getPrisaoInfo().getString("Itens.TempoDePrisao.Name"),
                lorePrisionTime,
                getPrisaoInfo().getInt("Itens.TempoDePrisao.Id"),
                (short) getPrisaoInfo().getInt("Itens.TempoDePrisao.Data")
        );

        ItemStack myDebts = ItemUtils.createItemMenu(
                getPrisaoInfo().getString("Itens.MinhasDividas.Name"),
                getPrisaoInfo().getStringList("Itens.MinhasDividas.Lore"),
                getPrisaoInfo().getInt("Itens.MinhasDividas.Id"),
                (short) getPrisaoInfo().getInt("Itens.MinhasDividas.Data")
        );

        inv.setItem(getPrisaoInfo().getInt("Itens.MinhasInformacoes.Slot"), myInformations);
        inv.setItem(getPrisaoInfo().getInt("Itens.TempoDePrisao.Slot"), prisionTime);
        inv.setItem(getPrisaoInfo().getInt("Itens.MinhasDividas.Slot"), myDebts);

        return inv;
    }

    private static String getPrisonTimeText(int time) {
        if (time < 60)
            return "{minutes} minuto(s)"
                    .replace("{minutes}", String.valueOf(time));
        else if (time < 1440) {
            int hours = time / 60;
            int minutes = time % 60;

            return "{hours} hora(s) e {minutes} minuto(s)"
                    .replace("{hours}", String.valueOf(hours))
                    .replace("{minutes}", String.valueOf(minutes));
        } else {
            int days = time / 1440;
            int hours = 0;
            int minutes = time % 1440;

            if (minutes >= 60) {
                hours = minutes / 60;
                minutes = minutes % 60;
            }

            return "{days} dias, {hours} hora(s) e {minutes} minuto(s)"
                    .replace("{days}", String.valueOf(days))
                    .replace("{hours}", String.valueOf(hours))
                    .replace("{minutes}", String.valueOf(minutes));
        }
    }
}
