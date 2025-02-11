package com.betafoprhoton.creativecomputerbugs.foundation.item.bugs;

import com.betafoprhoton.creativecomputerbugs.CCBMain.Companion.LOGGER
import com.betafoprhoton.creativecomputerbugs.foundation.util.isAPISupported
import com.betafoprhoton.creativecomputerbugs.registy.BugComputerHolderRegister.Companion.createWormComputerHolder
import com.betafoprhoton.creativecomputerbugs.registy.BugComputerHolderRegister.Companion.getBugComputerHolder
import com.simibubi.create.foundation.utility.RaycastHelper
import dan200.computercraft.shared.util.BlockEntityHelpers
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import org.jetbrains.annotations.NotNull

class WormItem(properties: Properties) : AbstractBugItem(properties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val blockEntity = context.level.getBlockEntity(context.clickedPos) ?: return super.useOn(context)
        println("Clicked on ${blockEntity.blockState.block} at ${context.clickedPos}")
        println("isAPISupported: ${blockEntity.isAPISupported()}")
        createWormComputerHolder(blockEntity, context.itemInHand, family)
            ?: run {
                println("Cannot create holder")
                super.useOn(context)
            }
        context.itemInHand.shrink(1)
        return InteractionResult.SUCCESS;
    }


    override fun inventoryTick(@NotNull itemStack: ItemStack, @NotNull level: Level, @NotNull entity: Entity, p_41407_: Int, p_41408_: Boolean) {
        super.inventoryTick(itemStack, level, entity, p_41407_, p_41408_)
        if (entity !is Player) return
        val ray = RaycastHelper.rayTraceUntil(entity, BlockEntityHelpers.DEFAULT_INTERACT_RANGE) {
            !level.getBlockState(it).isAir
        }
        if (ray.missed()) return
        val blockEntity = level.getBlockEntity(ray.pos) ?: return
        val holder = blockEntity.getBugComputerHolder()
        if (holder == null && blockEntity.isAPISupported())
            isActive = true
    }
}
