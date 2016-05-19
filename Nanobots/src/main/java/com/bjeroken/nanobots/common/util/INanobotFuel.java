package com.bjeroken.nanobots.common.util;

/*
 * Intended to use for player/armor/machine interactions.
 */
public interface INanobotFuel {
    public int getCurrentFuel();
    public int addFuel(int amount);
    public int consumeFuel(int amount);
    public boolean canConsumeFullFuel(int amount);
}
