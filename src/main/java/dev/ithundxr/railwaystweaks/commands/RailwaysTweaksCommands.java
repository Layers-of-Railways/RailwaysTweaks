package dev.ithundxr.railwaystweaks.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import dev.ithundxr.railwaystweaks.mixin.compat.tconstruct.SimpleChannelAccessor;
import me.pepperbell.simplenetworking.C2SPacket;
import me.pepperbell.simplenetworking.S2CPacket;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import slimeknights.tconstruct.common.network.TinkerNetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.commands.Commands.literal;

public class RailwaysTweaksCommands {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("railwaystweaks")
                .then($dump_hephaestus_packets()));
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("railwaystweaks")
                    .then($dump_trains()));
        });
    }

    private static ArgumentBuilder<CommandSourceStack, ?> $dump_hephaestus_packets() {
        return literal("dump_hephaestus_packets")
            .requires(cs -> cs.hasPermission(2))
            .executes(ctx -> dumpHephaestusPackets(ctx.getSource()));
    }

    private static ArgumentBuilder<CommandSourceStack, ?> $dump_trains() {
        return literal("dump_create_trains")
                .requires(cs -> cs.hasPermission(2))
                .executes(ctx -> dumpCreateTrains(ctx.getSource()));
    }

    private static int dumpHephaestusPackets(CommandSourceStack source) {
        TinkerNetwork tinkerNetwork = TinkerNetwork.getInstance();

        Map<Integer, Class<? extends S2CPacket>> idToS2C = new HashMap<>();
        Map<Integer, Class<? extends C2SPacket>> idToC2S = new HashMap<>();
        ((SimpleChannelAccessor) tinkerNetwork.network).getS2cIdMap().forEach((packet, id) -> {
            idToS2C.put(id, packet);
        });
        ((SimpleChannelAccessor) tinkerNetwork.network).getC2sIdMap().forEach((packet, id) -> {
            idToC2S.put(id, packet);
        });
        List<Integer> sortedS2CIds = new ArrayList<>(idToS2C.keySet());
        List<Integer> sortedC2SIds = new ArrayList<>(idToC2S.keySet());
        sortedS2CIds.sort(Integer::compareTo);
        sortedC2SIds.sort(Integer::compareTo);

        StringBuilder sb = new StringBuilder();
        sb.append("Hephaestus S2C packets:\n");
        sortedS2CIds.forEach(id -> {
            var packet = idToS2C.get(id);
            sb.append(id).append(" -> ").append(packet.getName()).append("\n");
        });

        sb.append("\n");
        sb.append("Hephaestus C2S packets:\n");
        sortedC2SIds.forEach(id -> {
            var packet = idToC2S.get(id);
            sb.append(id).append(" -> ").append(packet.getName()).append("\n");
        });

        source.sendSuccess(() -> Component.literal(sb.toString()), true);

        return 0;
    }

    private static int dumpCreateTrains(CommandSourceStack source) {
        StringBuilder s = new StringBuilder();

        Create.RAILWAYS.trains.forEach((uuid, train) -> {
            s.append("Train Name: ").append(train.name.toString());

            CarriageContraptionEntity entity = train.carriages.get(0).anyAvailableEntity();

            if (entity != null) {
                s.append(", is at ").append(entity.position());
            }

            s.append("\n");
        });

        source.sendSuccess(() -> Component.literal(s.toString()), true);

        return 0;
    }
}
