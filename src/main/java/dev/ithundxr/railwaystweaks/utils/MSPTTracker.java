package dev.ithundxr.railwaystweaks.utils;

import dev.ithundxr.railwaystweaks.RailwaysTweaks;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public class MSPTTracker {
    private final int averageIntervalTicks;
    private long lastTickTime = System.nanoTime();
    private double totalMSPT = 0;
    private double averageMSPT = 30;
    private int tickCount = 0;

    public MSPTTracker(int averageIntervalTicks) {
        this.averageIntervalTicks = averageIntervalTicks;
        ServerTickEvents.END_SERVER_TICK.register(this::endServerTick);
    }

    private void endServerTick(MinecraftServer server) {
        long now = System.nanoTime();
        long tickDuration = now - lastTickTime;
        double mspt = tickDuration / 1_000_000.0;
        lastTickTime = now;

        // Accumulate the MSPT and increment the tick counter
        totalMSPT += mspt;
        tickCount++;

        // Calculate the average MSPT every AVERAGE_INTERVAL_TICKS
        if (tickCount >= averageIntervalTicks) {
            averageMSPT = totalMSPT / tickCount;
            RailwaysTweaks.LOGGER.info("MSPT: " + averageMSPT);

            // Reset for the next interval
            totalMSPT = 0;
            tickCount = 0;
        }
    }

    public double getAverageMSPT() {
        return averageMSPT;
    }
}
