package com.github.natanfoleto.kabanaprisao.commands;

import com.github.natanfoleto.kabanaprisao.entities.Prision;
import com.github.natanfoleto.kabanaprisao.managers.PrisionManager;
import com.github.natanfoleto.kabanaprisao.storages.PrisionStorage;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.natanfoleto.kabanaprisao.loaders.SettingsLoader.*;

public class SetPrision {
    @Command(
            name = "setprision",
            aliases = {"setprisao", "addprisao", "adicionarprisao"},
            permission = "kabanaprisao.setprision",
            target = CommandTarget.PLAYER,
            usage = "setprisao <nome>"
    )

    public void onSetPrision(
            Context<Player> context,
            String name
    ) {
        Player p = context.getSender();

        Prision prision;

        if (PrisionStorage.getPrisions().containsKey(name)) {
            p.sendMessage(getMessages().getString("Prisao.Existe"));

            return;
        } else {
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

            prision = new Prision(name, iconSlot.intValue(), p.getLocation());
        }

        PrisionStorage.setPrision(name, prision);
        PrisionManager.createPrisionYaml(prision);

        p.sendMessage(getMessages().getString("Prisao.Criada"));
    }
}
