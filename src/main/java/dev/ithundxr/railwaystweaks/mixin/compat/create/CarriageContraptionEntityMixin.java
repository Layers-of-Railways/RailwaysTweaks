package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.entity.TrainStatus;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = CarriageContraptionEntity.class, remap = false)
public class CarriageContraptionEntityMixin extends OrientedContraptionEntity {

    @Shadow private Carriage carriage;

    public CarriageContraptionEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @WrapOperation(
            method = "startControlling",
            at = @At(
                    value = "INVOKE",
                    target="Lcom/simibubi/create/content/trains/entity/TrainStatus;manualControls()V"
            )
    )
    public void changeManualControlsMessage(TrainStatus instance, Operation<Void> original, BlockPos controlsLocalPos, Player player) {
        Train train = carriage.train;
        List<Component> queuedStatus = ((TrainStatusAccessor) train.status).getQueued();
        MutableComponent message = Lang.translateDirect("train.status.paused_for_manual");
        MutableComponent component =
                message.getString().equals("Schedule paused for manual controls")
                    ? message.append(" by " + player.getName().getString())
                    : Component.literal("[" + player.getName().getString() + "] ").append(message);

        queuedStatus.add(Components.literal(" - ").withStyle(ChatFormatting.GRAY)
            .append(component.withStyle(st -> st.withColor(0xD5ECC2))));

        if (queuedStatus.size() > 3) queuedStatus.remove(0);
    }
}
