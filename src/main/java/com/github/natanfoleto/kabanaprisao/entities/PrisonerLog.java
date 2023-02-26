package com.github.natanfoleto.kabanaprisao.entities;

public class PrisonerLog {
    private String name;
    private int timesArrested;

    public PrisonerLog(
            String name,
            int timesArrested
    ) {
        this.name = name;
        this.timesArrested = timesArrested;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getTimesArrested() { return timesArrested; }
    public void setTimesArrested(int timesArrested) { this.timesArrested = timesArrested; }
    public void incrementTimesArrested() { this.timesArrested = timesArrested + 1; }
}
