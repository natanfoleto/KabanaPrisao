package com.github.natanfoleto.kabanaprisao.commands;

import com.github.natanfoleto.kabanaprisao.database.repositories.PrisonerRepository;
import com.github.natanfoleto.kabanaprisao.entities.Prisoner;
import com.github.natanfoleto.kabanaprisao.storages.PrisionStorage;
import com.github.natanfoleto.kabanaprisao.storages.PrisonersStorage;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.ystoreplugins.yeconomy.api.YEconomyAPI;

import java.text.NumberFormat;
import java.util.Locale;

import static com.github.natanfoleto.kabanaprisao.loaders.SettingsLoader.*;

public class Bail {
    @Command(
            name = "bail",
            aliases = {"fianca", "pagarfianca", "fiancapagar"},
            permission = "kabanaprisao.bail",
            target = CommandTarget.PLAYER
    )

    public void onBail(Context<Player> context) {
        Player p = context.getSender();

        if (!YEconomyAPI.hasAccount(p.getName())) {
            p.sendMessage("§cErro ao buscar seu saldo.");

            return;
        }

        Prisoner prisoner = PrisonersStorage.getPrisoners().get(p.getName());

        if (prisoner == null) {
            p.sendMessage(getMessages().getString("Prisao.VoceNaoEstaPreso"));

            return;
        }

        double balance = YEconomyAPI.getBalance(p.getName());

        if (balance < prisoner.getBail()) {
            p.sendMessage(getMessages().getString("Prisao.SaldoInsuficiente"));

            return;
        }

        YEconomyAPI.withdraw(p.getName(), prisoner.getBail(), true);

        PrisonersStorage.delPrisoner(p.getName());
        PrisonerRepository.updateStatus(p.getName());

        p.teleport(PrisionStorage.getExitLocation());
        p.sendMessage(getMessages().getString("Prisao.PagouFianca"));
        p.playSound(p.getLocation(), Sound.PORTAL_TRAVEL, 1, 2f);

        if (getConfig().getBoolean("AlertaPagouFianca")) {
            Locale localeBR = new Locale("pt","BR");
            NumberFormat currencyBail = NumberFormat.getCurrencyInstance(localeBR);

            Bukkit.broadcastMessage(
                    getMessages().getString("Prisao.AlertaPagouFianca")
                            .replace("{name}", p.getName())
                            .replace("{bail}", currencyBail.format(prisoner.getBail()))
            );
        }
    }
}
