package com.beta_foprhoton.creativecomputerbugs.foundation.item;

import com.beta_foprhoton.creativecomputerbugs.CCBMain;
import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.core.BugComputerHolder;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity;
import com.simibubi.create.foundation.utility.RaycastHelper;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import dan200.computercraft.shared.util.BlockEntityHelpers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DebugStick extends Item {
    private boolean isActive;
    public DebugStick(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int vanillaIndex, boolean isVanillaIndex) {
        isActive = false;
        if (!(entity instanceof Player)) return;
        var ray = RaycastHelper.rayTraceUntil((Player) entity, BlockEntityHelpers.DEFAULT_INTERACT_RANGE, pos -> !level.getBlockState(pos).isAir());
        if (ray.missed()) return;
        var blockEntity = level.getBlockEntity(ray.getPos());
        if (blockEntity == null) return;
        var holder = BugComputerHolder.getBugComputerHolder(blockEntity);
        if (holder != null)
            isActive = true;
        itemStack.getOrCreateTag().putBoolean("isActive", isActive);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        if (!(context.getPlayer() instanceof ServerPlayer)) return super.useOn(context);
        var blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
        if (blockEntity == null) return super.useOn(context);
        var holder = BugComputerHolder.getBugComputerHolder(blockEntity);
        if (holder == null || context.getLevel().isClientSide || !holder.isUsable(context.getPlayer())) return super.useOn(context);
        if (context.getPlayer().isShiftKeyDown()) {
            var computer = holder.createServerComputer();
            computer.turnOn();
            new ComputerContainerData(computer, holder.getBugItem()).open(context.getPlayer(), holder);
        } else {
            CCBMain.BUG_COMPUTER_HOLDER_REGISTER.remove(holder.id);
        }
        return InteractionResult.SUCCESS;
    }
}
