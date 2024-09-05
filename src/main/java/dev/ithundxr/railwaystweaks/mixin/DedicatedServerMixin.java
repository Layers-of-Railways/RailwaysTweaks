package dev.ithundxr.railwaystweaks.mixin;

import dev.ithundxr.railwaystweaks.RailwaysTweaks;
import net.minecraft.CrashReport;
import net.minecraft.Util;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

@Mixin(value = DedicatedServer.class, priority = Integer.MAX_VALUE)
public abstract class DedicatedServerMixin {
    @Shadow @Final static Logger LOGGER;
    
    @Inject(method = "onServerExit", at = @At("TAIL"))
    private void railwaysTweaks$threadDumpOnShutdown(CallbackInfo ci) {
        if (Boolean.getBoolean("railwaystweaks.shutdown.dumpthreads")) {
            MinecraftServer self = ((MinecraftServer) (Object) this);
            
            ThreadMXBean threadmxbean = ManagementFactory.getThreadMXBean();
            ThreadInfo[] athreadinfo = threadmxbean.dumpAllThreads(true, true);
            StringBuilder stringbuilder = new StringBuilder();
            Error error = new Error("Shutdown Thread Dump");

            for (ThreadInfo threadinfo : athreadinfo) {
                if (threadinfo.getThreadId() == self.getRunningThread().getId()) {
                    error.setStackTrace(threadinfo.getStackTrace());
                }

                stringbuilder.append(threadinfo);
                stringbuilder.append("\n");
            }

            CrashReport crashreport = new CrashReport("Server shutdown thread dump", error);
            self.fillSystemReport(crashreport.getSystemReport());
            crashreport.addCategory("Thread Dump").setDetail("Threads", stringbuilder);
            Bootstrap.realStdoutPrintln("Crash report:\n" + crashreport.getFriendlyReport());
            File file1 = new File(new File(self.getServerDirectory(), "shutdown-thread-dumps"), "thread-dump-" + Util.getFilenameFormattedDateTime() + "-server.txt");
            if (crashreport.saveToFile(file1)) {
                LOGGER.error("This thread dump has been saved to: {}", file1.getAbsolutePath());
            } else {
                LOGGER.error("We were unable to save this crash report to disk.");
            }
        }

        if (Boolean.getBoolean("railwaystweaks.shutdown.forcefully")) {
            RailwaysTweaks.LOGGER.info("Force shutting down server, threads will be stuck");
            Runtime.getRuntime().halt(0);
        }
    }
}
