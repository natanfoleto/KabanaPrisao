package com.github.natanfoleto.kabanaprisao.commands;

import com.github.natanfoleto.kabanaprisao.database.repositories.PrisonerRepository;
import com.github.natanfoleto.kabanaprisao.entities.Prision;
import com.github.natanfoleto.kabanaprisao.entities.Prisoner;
import com.github.natanfoleto.kabanaprisao.schedulers.PrisonCooldown;
import com.github.natanfoleto.kabanaprisao.storages.PrisionStorage;
import com.github.natanfoleto.kabanaprisao.storages.PrisonersStorage;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.natanfoleto.kabanaprisao.loaders.SettingsLoader.*;

public class Arrest {
    @Command(
            name = "arrest",
            aliases = {"prender"},
            permission = "kabanaprisao.arrest",
            usage = "prender <jogador> <tempo> [prisao]"
    )

    public void onArrest(
            Context<CommandSender> context,
            String targetName,
            String time,
            @Optional String prisionName
    ) {
        CommandSender sender = context.getSender();

        if (time.equals("0")) return; // Setar tempo permanente

        int prisonTime = parseTimeToMinutes(time);

        if (prisonTime == 0) {
            context.sendMessage("§cO tempo está incorreto. Use um dos exemplos abaixo:");
            context.sendMessage("§71m -> 1 minuto");
            context.sendMessage("§71d -> 1 dia");
            context.sendMessage("§71mm -> 1 mês");

            return;
        }

        if (PrisonersStorage.getPrisoners().containsKey(targetName)) {
            sender.sendMessage(getMessages().getString("Prisao.JogadorJaPreso"));
            return;
        }

        if (PrisionStorage.getPrisions().isEmpty()) {
            sender.sendMessage(getMessages().getString("Prisao.NenhumaPrisaoCadastrada"));
            return;
        }

        Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            sender.sendMessage(getMessages().getString("Outras.JogadorOffline"));

            return;
        }

        Prision prision = null;

        if (prisionName != null) {
            prision = PrisionStorage.getPrisions().get(prisionName);

            if (prision == null) {
                sender.sendMessage(getMessages().getString("Prisao.NaoExiste"));

                return;
            }
        }

        if (prision == null) {
            for (Prision targetPrision : PrisionStorage.getPrisions().values()) {
                boolean isUsed = false;

                for (Prisoner prisoner : PrisonersStorage.getPrisoners().values()) {
                    if (targetPrision == prisoner.getPrision()) {
                        isUsed = true;
                        break;
                    }
                }

                if (!isUsed)
                    prision = targetPrision;
            }
        }

        if (prision == null) {
            sender.sendMessage(getMessages().getString("Prisao.NenhumaPrisaoVazia"));
            return;
        }

        List<Integer> slots = new ArrayList<>();
        AtomicInteger iconSlot = new AtomicInteger(0);

        for (Map.Entry<String, Prisoner> pair : PrisonersStorage.getPrisoners().entrySet())
            slots.add(pair.getValue().getIconSlot());

        Collections.sort(slots);

        for (Integer slot : slots) {
            if (slot == iconSlot.intValue())
                iconSlot.getAndIncrement();
            else
                break;
        }

        Prisoner prisoner = new Prisoner(
                targetName,
                target,
                prision,
                iconSlot.intValue(),
                prisonTime
        );

        PrisonersStorage.setPrisoner(targetName, prisoner);
        PrisonerRepository.createPrisoner(prisoner);
        PrisonCooldown.create(targetName, prisonTime);

        target.teleport(prision.getLocation());

        target.sendMessage(
                getMessages().getString("Prisao.FoiPreso")
                        .replace("{time}", time)
        );

        target.playSound(target.getLocation(), Sound.AMBIENCE_CAVE, 1, 2f);

        sender.sendMessage(
                getMessages().getString("Prisao.Prendeu")
                        .replace("{prision}", prision.getName())
        );

        if (getConfig().getBoolean("AlertaJogadorPreso"))
            for (String item : getMessages().getStringList("Prisao.AlertaPreso")) {
                Bukkit.broadcastMessage(
                        item
                                .replace("{name}", targetName)
                                .replace("{time}", time)
                );
            }
    }

    private static int parseTimeToMinutes(String time) {
        StringBuilder sbNumber = new StringBuilder();
        StringBuilder sbLetter = new StringBuilder();

        for (char c : time.toCharArray()) {
            if (Character.isDigit(c)) sbNumber.append(c);
            if (Character.isLetter(c)) sbLetter.append(c);
        }

        if (sbNumber.toString().isEmpty() || sbLetter.toString().isEmpty())
            return 0;

        int number = Integer.parseInt(sbNumber.toString());
        String letter = sbLetter.toString();

        int value = 0;

        if (letter.equalsIgnoreCase("m")) value = number;
        if (letter.equalsIgnoreCase("h")) value = number * 60;
        if (letter.equalsIgnoreCase("d")) value = number * 1440;
        if (letter.equalsIgnoreCase("mm")) value = number * 43200;

        return value;
    }
}
