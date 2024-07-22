package net.aluminiumtn.oldraid;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.event.GameEvent;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;

public class OldRaidHandler {

    public static void register() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((server, entity, killedEntity) -> {
            if (killedEntity instanceof PillagerEntity pillager) {
                if (isFlaggedPillager(pillager)) {
                    if (entity instanceof ServerPlayerEntity player) {
                        applyOrIncreaseBadOmen(player);
                    }
                }
            }
        });

        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof ItemEntity itemEntity) {
                if (itemEntity.getStack().getItem() == Items.OMINOUS_BOTTLE) {
                    itemEntity.discard(); 
                }
            }
        });
    }

    private static boolean isFlaggedPillager(PillagerEntity pillager) {
        ItemStack mainHandItem = pillager.getMainHandStack();
        ItemStack headItem = pillager.getEquippedStack(EquipmentSlot.HEAD);
        return headItem.getItem() == Items.WHITE_BANNER;
    }

    private static void applyOrIncreaseBadOmen(ServerPlayerEntity player) {
        StatusEffectInstance currentEffect = player.getStatusEffect(StatusEffects.BAD_OMEN);
        if (currentEffect != null) {
            int currentLevel = currentEffect.getAmplifier();
            if (currentLevel < 4) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.BAD_OMEN, 6000, currentLevel + 1));
            }
        } else {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BAD_OMEN, 6000, 0));
        }
    }
}
