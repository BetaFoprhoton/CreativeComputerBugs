package com.beta_foprhoton.creativecomputerbugs.foundation.item.bugs;

import com.beta_foprhoton.creativecomputerbugs.CCBMain
import com.beta_foprhoton.creativecomputerbugs.foundation.helpers.extensions.BlockEntityExtensions.getBugComputerHolder
import com.beta_foprhoton.creativecomputerbugs.foundation.helpers.extensions.BlockEntityExtensions.isAPISupported
import com.beta_foprhoton.creativecomputerbugs.foundation.helpers.extensions.ItemStackExtensions.getUpgradeWithData
import com.beta_foprhoton.creativecomputerbugs.foundation.helpers.extensions.ItemStackExtensions.setUpgrade
import com.simibubi.create.foundation.utility.RaycastHelper
import dan200.computercraft.shared.util.BlockEntityHelpers
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import org.jetbrains.annotations.NotNull

class BlockBugWormItem(properties: Properties) : AbstractBugItem(properties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val blockEntity = context.level.getBlockEntity(context.clickedPos) ?: return super.useOn(context)
        blockEntity.getBugComputerHolder() ?: return super.useOn(context)
        context.itemInHand.setUpgrade(null)
        CCBMain.BUG_COMPUTER_HOLDER_REGISTER.create(blockEntity, context.itemInHand.getUpgradeWithData(), family, context.clickedFace) ?: return super.useOn(context)
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
