package dev.ithundxr.railwaystweaks.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import dev.ithundxr.railwaystweaks.RailwaysTweaks;
import dev.ithundxr.railwaystweaks.mixin.compat.tconstruct.SimpleChannelAccessor;
import dev.ithundxr.railwaystweaks.utils.UUIDFinder;
import me.pepperbell.simplenetworking.C2SPacket;
import me.pepperbell.simplenetworking.S2CPacket;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.network.chat.Component;
import slimeknights.tconstruct.common.network.TinkerNetwork;
import xaero.pac.common.server.api.OpenPACServerAPI;
import xaero.pac.common.server.parties.party.api.IServerPartyAPI;

import java.util.*;

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

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("avgmspt")
                    .requires(cs -> cs.hasPermission(2))
                    .executes(ctx -> avgMSPT(ctx.getSource())));
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("uuid")
                    .then(Commands.argument("player_name", StringArgumentType.string())
                            .executes(RailwaysTweaksCommands::getPlayerUUID)));
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("railwaystweaks")
                    .then(
                            literal("opac-party")
                                    .then(Commands.argument("player_uuid", UuidArgument.uuid())
                                            .executes(RailwaysTweaksCommands::getPlayerPartyName))));
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
            s.append("Train Name: ").append(train.name.getString());
            s.append(", Position: ").append(train.carriages.get(0).getLeadingPoint().getPosition(train.graph));
            s.append("\n");
        });

        source.sendSuccess(() -> Component.literal(s.toString()), true);

        return 0;
    }

    private static int avgMSPT(CommandSourceStack source) {
        source.sendSuccess(
                () -> Component.literal(
                        "Average MSPT (10s): " + String.format("%.1f", RailwaysTweaks.MSPT_TRACKER.getAverageMSPT())),
                true);
        return 0;
    }

    private static int getPlayerPartyName(CommandContext<CommandSourceStack> ctx) {
        UUID uuid = UuidArgument.getUuid(ctx, "player_uuid");

        OpenPACServerAPI api = OpenPACServerAPI.get(ctx.getSource().getServer());
        IServerPartyAPI partyAPI = api.getPartyManager().getPartyByMember(uuid);

        if (partyAPI != null) {
            ctx.getSource().sendSuccess(() -> Component.literal(partyAPI.getDefaultName() + "\n" + partyAPI.getId()),
                    false);
            return 0;
        } else {
            ctx.getSource().sendFailure(Component.literal("Failed to get a party uuid from this player"));
            return 1;
        }
    }

    private static int getPlayerUUID(CommandContext<CommandSourceStack> ctx) {
        String name = StringArgumentType.getString(ctx, "player_name");

        if (name != null) {
            UUIDFinder.findUuid(name, (uuid) -> {
                if (uuid != null)
                    ctx.getSource().sendSuccess(() -> Component.literal(uuid.toString()), false);
            });
        }
        return 0;
    }
}
