package com.beta_foprhoton.creativecomputerbugs.foundation.item;

import com.beta_foprhoton.creativecomputerbugs.CCBMain;
import com.beta_foprhoton.creativecomputerbugs.foundation.helpers.extensions.getBugComputerHolder
import com.beta_foprhoton.creativecomputerbugs.foundation.helpers.extensions.isAPISupported
import com.simibubi.create.foundation.utility.RaycastHelper;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import dan200.computercraft.shared.util.BlockEntityHelpers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level;

class DebugStick(properties: Properties): Item(properties) {
    private var isActive = false
        set(value) = if(value) setActive() else setInActive()


    override fun inventoryTick(itemStack: ItemStack, level: Level, entity: Entity, p_41407_: Int, p_41408_: Boolean) {
        if (entity !is Player) return
        val ray = RaycastHelper.rayTraceUntil(entity, BlockEntityHelpers.DEFAULT_INTERACT_RANGE) {
            !level.getBlockState(it).isAir
        }
        if (ray.missed()) return
        val blockEntity = level.getBlockEntity(ray.pos) ?: return
        val holder = blockEntity.getBugComputerHolder()
        if (holder == null && blockEntity.isAPISupported())
            isActive = true
        itemStack.getOrCreateTag().putBoolean("isActive", isActive);
    }

    fun setActive() {

    }

    fun setInActive() {

    }

    override fun useOn(context: UseOnContext): InteractionResult {
        if (context.player !is ServerPlayer) return super.useOn(context)
        val blockEntity = context.level.getBlockEntity(context.clickedPos) ?: return super.useOn(context)
        val holder = blockEntity.getBugComputerHolder() ?: return super.useOn(context)
        if (context.level.isClientSide || !holder.isUsableByPlayer(context.player as ServerPlayer)) return super.useOn(context)
        if ((context.player as ServerPlayer).isShiftKeyDown) {
            val computer = holder.createServerComputer()
            computer.turnOn();
            ComputerContainerData(computer, holder.getBug()).open(context.player, holder)
        } else {
            CCBMain.BUG_COMPUTER_HOLDER_REGISTER.remove(holder.id)
        }
        return InteractionResult.SUCCESS
    }

    override fun interactLivingEntity(itemStack: ItemStack, player: Player, entity: LivingEntity, hand: InteractionHand): InteractionResult {
        if (player !is ServerPlayer) return InteractionResult.PASS
        val holder = entity.getBugComputerHolder() ?: return InteractionResult.PASS
        if (holder.isUsableByPlayer(player)) return InteractionResult.PASS
        if (player.isShiftKeyDown) {
            val computer = holder.createServerComputer()
            computer.turnOn()
            ComputerContainerData(computer, holder.getBug()).open(player, holder)
        } else {
            CCBMain.BUG_COMPUTER_HOLDER_REGISTER.remove(holder.id)
        }
        return InteractionResult.SUCCESS
    }
}
