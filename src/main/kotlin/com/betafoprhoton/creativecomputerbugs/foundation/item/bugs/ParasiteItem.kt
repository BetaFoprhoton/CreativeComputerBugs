package com.betafoprhoton.creativecomputerbugs.foundation.item.bugs

import com.betafoprhoton.creativecomputerbugs.registy.BugComputerHolderRegister.Companion.createParasiteComputerHolder
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class ParasiteItem(properties: Properties): AbstractBugItem(properties) {
    override fun interactLivingEntity(itemStack: ItemStack, player: Player, livingEntity: LivingEntity, hand: InteractionHand): InteractionResult {
        createParasiteComputerHolder(livingEntity, itemStack, family) ?: return super.interactLivingEntity(itemStack, player, livingEntity, hand)
        itemStack.shrink(1)
        return InteractionResult.SUCCESS
    }
}