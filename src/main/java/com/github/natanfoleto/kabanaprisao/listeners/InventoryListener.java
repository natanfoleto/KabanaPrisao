package com.github.natanfoleto.kabanaprisao.listeners;

import com.github.natanfoleto.kabanaprisao.entities.Prision;
import com.github.natanfoleto.kabanaprisao.entities.Prisoner;
import com.github.natanfoleto.kabanaprisao.storages.PrisionStorage;
import com.github.natanfoleto.kabanaprisao.storages.PrisonersStorage;
import com.github.natanfoleto.kabanaprisao.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import static com.github.natanfoleto.kabanaprisao.loaders.MenuLoader.*;

public class InventoryListener implements Listener {
    @EventHandler
    public void onClickInventory(InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
            return;

        Player p = (Player) e.getWhoClicked();

        if (
                e.getView().getTitle().equalsIgnoreCase(getPrincipal().getString("Nome")) ||
                e.getView().getTitle().equalsIgnoreCase(getPrisoes().getString("Nome")) ||
                e.getView().getTitle().equalsIgnoreCase(getPresos().getString("Nome")) ||
                e.getView().getTitle().equalsIgnoreCase(getPrisaoInfo().getString("Nome"))
        ) {
            e.setCancelled(true);

            // Menu principal aberto
            if (e.getView().getTitle().equalsIgnoreCase(getPrincipal().getString("Nome"))) {
                // Clicou no botão de prisões
                if (e.getSlot() == getPrincipal().getInt("Itens.Prisoes.Slot")) {
                    Inventory inv = InventoryUtils.createPrisonsMenu();

                    p.openInventory(inv);
                    p.playSound(p.getLocation(), Sound.CLICK, 1, 2f);

                    return;
                }

                // Clicou no botão de presos
                if (e.getSlot() == getPrincipal().getInt("Itens.Presos.Slot")) {
                    Inventory inv = InventoryUtils.createPrisonersMenu();

                    p.openInventory(inv);
                    p.playSound(p.getLocation(), Sound.CLICK, 1, 2f);

                    return;
                }
            }

            // Menu de prisões aberto
            if (e.getView().getTitle().equalsIgnoreCase(getPrisoes().getString("Nome"))) {
                // Clicou no botão voltar
                if (e.getSlot() == getPrisoes().getInt("Voltar.Slot")) {
                    Inventory inv = InventoryUtils.createMainMenu();

                    p.openInventory(inv);
                    p.playSound(p.getLocation(), Sound.CLICK, 1, 2f);

                    return;
                }

                // Pega a prisão clicada
                Prision prision = PrisionStorage
                        .getPrisions()
                        .values()
                        .stream()
                        .filter(item -> item.getIconSlot() == e.getSlot())
                        .findFirst()
                        .orElse(null);

                if (prision == null)
                    return;

                // Clicou numa prisão com botão esquerdo (Teleporta até a prisão)
                if (e.isLeftClick()) {
                    p.closeInventory();
                    p.chat("/goprision " + prision.getName());
                    p.playSound(p.getLocation(), Sound.CLICK, 1, 2f);
                }

                // Clicou numa prisão com botão direito (Deleta a prisão)
                if (e.isRightClick())
                    p.chat("/unsetprision " + prision.getName());
            }

            // Menu de presos aberto
            if (e.getView().getTitle().equalsIgnoreCase(getPresos().getString("Nome"))) {
                // Clicou no botão voltar
                if (e.getSlot() == getPrisoes().getInt("Voltar.Slot")) {
                    Inventory inv = InventoryUtils.createMainMenu();

                    p.openInventory(inv);
                    p.playSound(p.getLocation(), Sound.CLICK, 1, 2f);

                    return;
                }

                // Pega o preso clicada
                Prisoner prisoner = PrisonersStorage
                        .getPrisoners()
                        .values()
                        .stream()
                        .filter(item -> item.getIconSlot() == e.getSlot())
                        .findFirst()
                        .orElse(null);

                if (prisoner == null)
                    return;

                // Clicou num preso com botão direito (Solta um preso)
                if (e.isRightClick())
                    p.chat("/release " + prisoner.getName());
            }

            // Menu de prisão info aberto
            if (e.getView().getTitle().equalsIgnoreCase(getPrisaoInfo().getString("Nome"))) {
                // Clicou na fiança (Paga a fiança)
                if (e.getSlot() == getPrisaoInfo().getInt("Itens.MinhasDividas.Slot"))
                    if (e.isLeftClick())
                        p.chat("/bail");
            }
        }
    }
}
