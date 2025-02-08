package com.betafoprhoton.creativecomputerbugs.foundation.item.bugs

import com.betafoprhoton.creativecomputerbugs.CCBMain
import com.betafoprhoton.creativecomputerbugs.foundation.helpers.extensions.ItemStackExtensions.getUpgradeWithData
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class ParasiteItem(properties: Properties): AbstractBugItem(properties) {
    override fun interactLivingEntity(itemStack: ItemStack, player: Player, livingEntity: LivingEntity, hand: InteractionHand): InteractionResult {
        val holder = CCBMain.BUG_COMPUTER_HOLDER_REGISTER.createParasiteComputerHolder(livingEntity, itemStack.getUpgradeWithData(), family) ?: return InteractionResult.PASS
        itemStack.shrink(1)
        return InteractionResult.SUCCESS
    }
}