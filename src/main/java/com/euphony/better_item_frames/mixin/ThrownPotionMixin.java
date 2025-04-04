package com.euphony.better_item_frames.mixin;


import java.util.List;

import com.euphony.better_item_frames.api.ICustomItemFrame;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownPotion.class)
public class ThrownPotionMixin {
    @Inject(method = "applySplash(Lnet/minecraft/server/level/ServerLevel;Ljava/lang/Iterable;Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"))
    public void applyInvisibility(ServerLevel level, Iterable<MobEffectInstance> effects, Entity p_entity, CallbackInfo ci) {
        ThrownPotion potion = (ThrownPotion) (Object) this;
        better_item_frames$handleInvisibility(effects, potion);
    }

    @Inject(method = "applyWater(Lnet/minecraft/server/level/ServerLevel;)V", at = @At("HEAD"))
    private void applyWater(CallbackInfo ci) {
        ThrownPotion potion = (ThrownPotion) (Object) this;
        better_item_frames$handleWater(potion);
    }

    @Unique
    private static void better_item_frames$handleInvisibility(Iterable<MobEffectInstance> effectInstanceList, ThrownPotion thrownPotion) {
        AABB checkBox = thrownPotion.getBoundingBox().inflate(2.0D, 1.0D, 2.0D);
        effectInstanceList.forEach(instance -> {
            if (instance.getEffect() == MobEffects.INVISIBILITY) {
                List<ItemFrame> itemFrames = thrownPotion.level().getEntitiesOfClass(ItemFrame.class, checkBox);
                for (ItemFrame frame : itemFrames) {
                    ICustomItemFrame itemFrame = (ICustomItemFrame) frame;
                    if (!itemFrame.better_item_frames$getIsInvisible())
                        itemFrame.better_item_frames$setIsInvisible(true);
                }
            }
        });
    }

    @Unique
    private static void better_item_frames$handleWater(ThrownPotion thrownPotion) {
        AABB checkBox = thrownPotion.getBoundingBox().inflate(2.0D, 1.0D, 2.0D);
        List<ItemFrame> itemFrames = thrownPotion.level().getEntitiesOfClass(ItemFrame.class, checkBox);
        for (ItemFrame frame : itemFrames) {
            ICustomItemFrame itemFrame = (ICustomItemFrame) frame;
            if (itemFrame.better_item_frames$getIsInvisible())
                itemFrame.better_item_frames$setIsInvisible(false);
        }
    }
}