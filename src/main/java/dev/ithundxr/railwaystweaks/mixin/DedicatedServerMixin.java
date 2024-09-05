package dev.ithundxr.railwaystweaks.mixin;

import com.google.common.collect.Streams;
import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.Util;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.net.Proxy;
import java.util.stream.Collectors;

@Mixin(value = DedicatedServer.class, priority = 100000000)
public abstract class DedicatedServerMixin extends MinecraftServer {
    @Unique private static final Logger railwaysTweaks$LOGGER = LogUtils.getLogger();

    public DedicatedServerMixin(Thread serverThread, LevelStorageSource.LevelStorageAccess storageSource, PackRepository packRepository, WorldStem worldStem, Proxy proxy, DataFixer fixerUpper, Services services, ChunkProgressListenerFactory progressListenerFactory) {
        super(serverThread, storageSource, packRepository, worldStem, proxy, fixerUpper, services, progressListenerFactory);
    }

    @Inject(method = "onServerExit", at = @At("TAIL"))
    private void railwaysTweaks$threadDumpOnShutdown(CallbackInfo ci) {
        ThreadMXBean threadmxbean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] athreadinfo = threadmxbean.dumpAllThreads(true, true);
        StringBuilder stringbuilder = new StringBuilder();
        Error error = new Error("Shutdown Thread Dump");

        for(ThreadInfo threadinfo : athreadinfo) {
            if (threadinfo.getThreadId() == this.getRunningThread().getId()) {
                error.setStackTrace(threadinfo.getStackTrace());
            }

            stringbuilder.append(threadinfo);
            stringbuilder.append("\n");
        }

        CrashReport crashreport = new CrashReport("Server shutdown thread dump", error);
        this.fillSystemReport(crashreport.getSystemReport());
        CrashReportCategory crashreportcategory = crashreport.addCategory("Thread Dump");
        crashreportcategory.setDetail("Threads", stringbuilder);
        CrashReportCategory crashreportcategory1 = crashreport.addCategory("Performance stats");
        crashreportcategory1.setDetail("Random tick rate", () -> this.getWorldData().getGameRules().getRule(GameRules.RULE_RANDOMTICKING).toString());
        crashreportcategory1.setDetail("Level stats", () -> Streams.stream(this.getAllLevels()).map((level) -> level.dimension() + ": " + level.getWatchdogStats()).collect(Collectors.joining(",\n")));
        Bootstrap.realStdoutPrintln("Crash report:\n" + crashreport.getFriendlyReport());
        File file1 = new File(new File(this.getServerDirectory(), "shutdown-thread-dumps"), "thread-dump-" + Util.getFilenameFormattedDateTime() + "-server.txt");
        if (crashreport.saveToFile(file1)) {
            railwaysTweaks$LOGGER.error("This crash report has been saved to: {}", file1.getAbsolutePath());
        } else {
            railwaysTweaks$LOGGER.error("We were unable to save this crash report to disk.");
        }
    }
}
