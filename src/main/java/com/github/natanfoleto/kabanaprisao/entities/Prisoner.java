package com.github.natanfoleto.kabanaprisao.entities;

import org.bukkit.entity.Player;

public class Prisoner {
    private String name;
    private Player player;
    private Prision prision;
    private int iconSlot;
    private int prisionTime;
    private int timeLeft;
    private int bail;
    private String reason;

    public Prisoner(
            String name,
            Player player,
            Prision prision,
            int iconSlot,
            int prisionTime,
            int bail,
            String reason
    ) {
        this.name = name;
        this.player = player;
        this.prision = prision;
        this.iconSlot = iconSlot;
        this.prisionTime = prisionTime;
        this.bail = bail;
        this.timeLeft = 0;
        this.reason = reason;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }

    public Prision getPrision() { return prision; }
    public void setPrision(Prision prision) { this.prision = prision; }

    public int getIconSlot() { return iconSlot; }
    public void setIconSlot(int iconSlot) { this.iconSlot = iconSlot; }

    public int getPrisionTime() { return prisionTime; }
    public void setPrisionTime(int prisonTime) { this.prisionTime = prisonTime; }

    public int getTimeLeft() { return timeLeft; }
    public void setTimeLeft(int timeLeft) { this.timeLeft = timeLeft; }

    public int getBail() { return bail; }
    public void setBail(int bail) { this.bail = bail; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
