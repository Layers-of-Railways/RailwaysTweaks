package dev.ithundxr.railwaystweaks.mixin.compat.dcintegration;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dcshadow.net.kyori.adventure.text.Component;
import dcshadow.net.kyori.adventure.text.TextReplacementConfig;
import de.erdbeerbaerlp.dcintegration.common.util.ComponentUtils;
import de.erdbeerbaerlp.dcintegration.common.util.McServerInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "de.erdbeerbaerlp.dcintegration.common.DiscordEventListener", remap = false)
public class DiscordEventListenerMixin {
    @WrapOperation(method = "onEvent", at = @At(value = "INVOKE", target = "Lde/erdbeerbaerlp/dcintegration/common/util/McServerInterface;sendIngameMessage(Ldcshadow/net/kyori/adventure/text/Component;)V"))
    private void railwayTweaks$addBlockedChars(McServerInterface instance, Component component, Operation<Void> original) {
        TextReplacementConfig replacementConfig = ComponentUtils.replaceLiteral("§", "[no]");
        original.call(instance, component.replaceText(replacementConfig));
    }
}
