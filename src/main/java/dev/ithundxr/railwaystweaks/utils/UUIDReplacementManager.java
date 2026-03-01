package dev.ithundxr.railwaystweaks.utils;

import com.google.common.collect.ImmutableMap;
import dev.ithundxr.railwaystweaks.RailwaysTweaks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UUIDReplacementManager {
    public static final Map<UUID, UUID> REPLACEMENTS = load();

    private static Map<UUID, UUID> load() {
        Map<UUID, UUID> map = new HashMap<>();

        // syntax is 'orig:replacement,orig2:replacement2,...'
        String param = System.getProperty("railwaystweaks.uuid_replacements");
        if (param != null) {
            String[] pairs = param.split(",");
            for (String pair : pairs) {
                if (pair.isBlank()) continue;

                String[] parts = pair.split(":");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid uuid replacement pair: '"+pair+"'. Expected format 'orig:replacement'");
                }

                try {
                    UUID orig = UUID.fromString(parts[0]);
                    UUID replacement = UUID.fromString(parts[1]);
                    map.put(orig, replacement);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid uuid in pair: '"+pair+"'. Expected format 'orig:replacement'", e);
                }
            }

            RailwaysTweaks.LOGGER.info("Loaded {} UUID replacements", map.size());
        }

        return ImmutableMap.copyOf(map);
    }

    public static void init() {}
}
