package dev.ithundxr.railwaystweaks.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class UUIDFinder {
    private static final String MOJANG_UUID_API = "https://api.mojang.com/users/profiles/minecraft/";
    private static final int TIMEOUT_IN_SECS = 3;

    private static CompletableFuture<UUID> uuidFuture;

    /**
     * Takes a users name and finds a matching UUID
     */
    public static void findUuid(String playerName, Consumer<UUID> finish) {
        // Asynchronously search for a users UUID
        uuidFuture = CompletableFuture.supplyAsync(() -> UUIDFinder.getUuidFromPlayer(playerName));
        uuidFuture.thenAccept(finish);
    }

    private static UUID getUuidFromPlayer(String playerName) {
        UUID uuid;

        try {
            URL uuidGetRequest = new URL(MOJANG_UUID_API + playerName);
            var httpURLConnection = (HttpURLConnection) uuidGetRequest.openConnection();
            httpURLConnection.setRequestMethod("GET");

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode != 200)
                uuidFuture.completeExceptionally(new IOException("Failed to get a valid UUID for the player"));

            var stringBuilder = new StringBuilder();
            var scanner = new Scanner(uuidGetRequest.openStream());
            while (scanner.hasNext())
                stringBuilder.append(scanner.nextLine());
            scanner.close();

            var uuidObject = (JsonObject) JsonParser.parseString(stringBuilder.toString());
            String id = uuidObject.get("id").getAsString();

            // Format into a proper uuid (accepted) uuid format complete with '-' characters
            String idFormatted = id.substring(0, 8) + "-" + id.substring(8, 12) + "-"
                    + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32);

            uuid = UUID.fromString(idFormatted);

        } catch (IOException e) {
            uuid = null;
        }
        return uuid;
    }
}
