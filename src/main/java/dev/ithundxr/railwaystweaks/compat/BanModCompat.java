package dev.ithundxr.railwaystweaks.compat;

import dev.ithundxr.railwaystweaks.RailwaysTweaks;
import eu.pb4.banhammer.api.BanHammer;
import eu.pb4.banhammer.api.PunishmentType;
import io.github.slimeistdev.acme_admin.api.v0.events.ACMEBanCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;

public class BanModCompat {
    private static final String dataFormat = "{\"uuid\": \"%s\"}";
    
    private static final String API_KEY;
    
    static {
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "railwayTweaks.properties");
        Properties properties = new Properties();

        if (configFile.exists()) {
            try (FileInputStream stream = new FileInputStream(configFile)) {
                properties.load(stream);
            } catch (IOException e) {
                RailwaysTweaks.LOGGER.warn("Could not read property file '{}'", configFile.getAbsolutePath(), e);
            }
        }

        API_KEY = (String) properties.computeIfAbsent("api-key", a -> "null");

        try (FileOutputStream stream = new FileOutputStream(configFile)) {
            properties.store(stream, null);
        } catch (IOException e) {
            RailwaysTweaks.LOGGER.warn("Could not store property file '{}'", configFile.getAbsolutePath(), e);
        }
    }
    
    public static void init() {
        BanHammer.registerPunishmentEvent((punishmentData, silent, invisible) -> {
            if (!punishmentData.isTemporary() && punishmentData.type == PunishmentType.BAN) {
                execute(punishmentData.playerUUID);
            }
        });

        ACMEBanCallback.EVENT.register((bannedPlayer, source, banCause, cancellable) -> {
            if (banCause.getExpiration() == null) {
                execute(bannedPlayer.getUUID());
            }
        });
    }
    
    public static void execute(UUID uuid) {
        if (!"null".equals(API_KEY)) {
            Util.ioPool().submit(() -> {
                HttpRequest request = HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(dataFormat.formatted(uuid)))
                        .header("Authorization", API_KEY)
                        .uri(URI.create("https://railways.ithundxr.dev/backend/minecraft/ban"))
                        .build();

                try {
                    HttpClient.newBuilder()
                            .connectTimeout(Duration.ofSeconds(10L))
                            .followRedirects(HttpClient.Redirect.ALWAYS)
                            .build()
                            .send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
                } catch (IOException | InterruptedException ignored) {}
            });
        }
    }
}
