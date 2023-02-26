package com.github.natanfoleto.kabanaprisao.entities;

import org.bukkit.Location;


public class Prision {
    private String name;
    private int iconSlot;
    private Location location;

    public Prision(String name, int iconSlot, Location location) {
        this.name = name;
        this.iconSlot = iconSlot;
        this.location = location;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getIconSlot() { return iconSlot; }
    public void setIconSlot(int iconSlot) { this.iconSlot = iconSlot; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
}
