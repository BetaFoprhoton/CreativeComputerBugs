package com.beta_foprhoton.creativecomputerbugs.foundation.item.bugs;

import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.block.AllBlockAPIs;
import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.core.BugComputerHolder;
import com.simibubi.create.content.kinetics.motor.CreativeMotorBlockEntity;
import com.simibubi.create.foundation.utility.RaycastHelper;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.upgrades.UpgradeData;
import dan200.computercraft.impl.PocketUpgrades;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.util.BlockEntityHelpers;
import dan200.computercraft.shared.util.NBTUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class AbstractBugItem extends Item {
    private static final String NBT_UPGRADE = "Upgrade";
    private static final String NBT_UPGRADE_INFO = "UpgradeInfo";
    private static final String NBT_IS_ACTIVE = "IsActive";
    public static final String INFECTED_BLOCK_FLAG = "InfectedBugComputerID";
    private boolean isActive;
    public final ComputerFamily family;
    public AbstractBugItem(Properties properties) {
        super(properties);
        family = ComputerFamily.ADVANCED;

    }

    @Override
    public void inventoryTick(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int p_41407_, boolean p_41408_) {
        isActive = false;
        if (!(entity instanceof Player)) return;
        var ray = RaycastHelper.rayTraceUntil((Player) entity, BlockEntityHelpers.DEFAULT_INTERACT_RANGE, pos -> !level.getBlockState(pos).isAir());
        if (ray.missed()) return;
        var blockEntity = level.getBlockEntity(ray.getPos());
        if (blockEntity == null) return;
        var holder = BugComputerHolder.getBugComputerHolder(blockEntity);
        if (holder == null && AllBlockAPIs.getSuitableAPI(blockEntity) != null)
            isActive = true;
        itemStack.getOrCreateTag().putBoolean(NBT_IS_ACTIVE, isActive);
    }

    public boolean isBlockSupported (BlockEntity blockEntity) {
        return blockEntity instanceof CreativeMotorBlockEntity;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        var offHandStack = player.getOffhandItem();
        return super.use(level, player, hand);
    }

    @Override
    public Component getName(ItemStack stack) {
        var baseString = getDescriptionId(stack);
        var upgrade = getUpgrade(stack);
        if (upgrade != null) {
            return Component.translatable(baseString + ".upgraded",
                    Component.translatable(upgrade.getUnlocalisedAdjective())
            );
        } else {
            return super.getName(stack);
        }
    }

    public static @Nullable IPocketUpgrade getUpgrade(ItemStack stack) {
        var compound = stack.getTag();
        if (compound == null || !compound.contains(NBT_UPGRADE)) return null;
        return PocketUpgrades.instance().get(compound.getString(NBT_UPGRADE));
    }

    public static @Nullable UpgradeData<IPocketUpgrade> getUpgradeWithData(ItemStack stack) {
        var compound = stack.getTag();
        if (compound == null || !compound.contains(NBT_UPGRADE)) return null;
        var upgrade = PocketUpgrades.instance().get(compound.getString(NBT_UPGRADE));
        return upgrade == null ? null : UpgradeData.of(upgrade, NBTUtil.getCompoundOrEmpty(compound, NBT_UPGRADE_INFO));
    }

    public static void setUpgrade(ItemStack stack, @Nullable UpgradeData<IPocketUpgrade> upgrade) {
        var compound = stack.getOrCreateTag();

        if (upgrade == null) {
            compound.remove(NBT_UPGRADE);
            compound.remove(NBT_UPGRADE_INFO);
        } else {
            compound.putString(NBT_UPGRADE, upgrade.upgrade().getUpgradeID().toString());
            compound.put(NBT_UPGRADE_INFO, upgrade.data().copy());
        }
    }

    public static CompoundTag getUpgradeInfo(ItemStack stack) {
        return stack.getOrCreateTagElement(NBT_UPGRADE_INFO);
    }
}
