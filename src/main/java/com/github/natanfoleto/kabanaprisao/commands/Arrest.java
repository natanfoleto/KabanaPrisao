package com.github.natanfoleto.kabanaprisao.commands;

import com.github.natanfoleto.kabanaprisao.database.repositories.PrisonerLogRepository;
import com.github.natanfoleto.kabanaprisao.database.repositories.PrisonerRepository;
import com.github.natanfoleto.kabanaprisao.entities.Prision;
import com.github.natanfoleto.kabanaprisao.entities.Prisoner;
import com.github.natanfoleto.kabanaprisao.schedulers.PrisonCooldown;
import com.github.natanfoleto.kabanaprisao.storages.PrisionStorage;
import com.github.natanfoleto.kabanaprisao.storages.PrisonersLogStorage;
import com.github.natanfoleto.kabanaprisao.storages.PrisonersStorage;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.natanfoleto.kabanaprisao.loaders.SettingsLoader.*;

public class Arrest {
    @Command(
            name = "arrest",
            aliases = {"prender"},
            permission = "kabanaprisao.arrest",
            usage = "prender <jogador> <tempo> [razao]"
    )

    public void onArrest(
            Context<CommandSender> context,
            String targetName,
            String time
    ) {
        CommandSender sender = context.getSender();

        if (time.equals("0")) {
            sender.sendMessage(getMessages().getString("Prisao.TempoMaiorQueZero"));

            return;
        }

        int prisonTime = parseTimeToMinutes(time);

        if (prisonTime == 0) {
            getMessages().getStringList("Prisao.TempoCorreto").forEach(context::sendMessage);

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

        if (prision == null) {
            sender.sendMessage(getMessages().getString("Prisao.NenhumaPrisaoVazia"));
            return;
        }

        String reason = "§cMotivo não informado.";

        if (context.getArgs().length > 2) {
            List<String> newArgs = new LinkedList<>(Arrays.asList(context.getArgs()));

            newArgs.remove(0);
            newArgs.remove(0);

            reason = String.join(" ", newArgs);
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
                prisonTime,
                reason
        );

        PrisonersStorage.setPrisoner(targetName, prisoner);
        PrisonCooldown.create(targetName, prisonTime);
        PrisonerRepository.createPrisoner(prisoner);

        if (PrisonerLogRepository.getCountUserByName(targetName) == 0)
            PrisonerLogRepository.createPrisonerLog(targetName);
        else {
            PrisonersLogStorage.incrementTimesArrested(targetName);
            PrisonerLogRepository.updateTimes(targetName);
        }

        target.teleport(prision.getLocation());

        target.sendMessage(
                getMessages().getString("Preso.FoiPreso")
                        .replace("{time}", time)
        );

        target.playSound(target.getLocation(), Sound.AMBIENCE_CAVE, 1, 2f);

        sender.sendMessage(
                getMessages().getString("Prisao.Prendeu")
                        .replace("{prision}", prision.getName())
        );

        if (getConfig().getBoolean("AlertaJogadorPreso"))
            for (String item : getMessages().getStringList("Preso.AlertaPreso")) {
                Bukkit.broadcastMessage(
                        item
                                .replace("{name}", targetName)
                                .replace("{time}", time)
                                .replace("{reason}", reason)
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
