package com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.core;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.upgrades.UpgradeData;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.pocket.items.PocketComputerItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

import static net.minecraft.world.level.block.Block.popResource;

public class InfectedBlockServerComputer extends ServerComputer implements IPocketAccess {
    private @Nullable IPocketUpgrade upgrade;
    private final BlockEntity infectedBlockEntity;
    private final ItemStack bugItem;

    public InfectedBlockServerComputer(@NotNull BlockEntity infectedBlockEntity, int computerID, @Nullable String label, ComputerFamily family, int terminalWidth, int terminalHeight, @NotNull ItemStack bugItem) {
        super((ServerLevel) infectedBlockEntity.getLevel(), infectedBlockEntity.getBlockPos(), computerID, label, family, terminalWidth, terminalHeight);
        this.infectedBlockEntity = infectedBlockEntity;
        this.bugItem = bugItem;
    }

    public BlockEntity getInfectedBlockEntity() {
        return infectedBlockEntity;
    }

    @Nullable
    @Override
    public Entity getEntity() {
        return null;
    }

    @Override
    public int getColour() {
        return -1;
    }

    @Override
    public void setColour(int colour) {

    }

    @Override
    public int getLight() {
        return -1;
    }

    @Override
    public void setLight(int colour) {

    }

    @Override
    public CompoundTag getUpgradeNBTData() {
        return PocketComputerItem.getUpgradeInfo(bugItem);
    }

    @Override
    public void updateUpgradeNBTData() {

    }

    @Override
    public void invalidatePeripheral() {
        var peripheral = upgrade == null ? null : upgrade.createPeripheral(this);
        setPeripheral(ComputerSide.BACK, peripheral);
    }

    @Override
    public Map<ResourceLocation, IPeripheral> getUpgrades() {
        return upgrade == null ? Map.of() : Collections.singletonMap(upgrade.getUpgradeID(), getPeripheral(ComputerSide.BACK));
    }

    public @Nullable UpgradeData<IPocketUpgrade> getUpgrade() {
        return upgrade == null ? null : UpgradeData.of(upgrade, getUpgradeNBTData());
    }

    public void setUpgrade(@Nullable UpgradeData<IPocketUpgrade> upgrade) {
        synchronized (this) {
            PocketComputerItem.setUpgrade(bugItem, upgrade);
            updateUpgradeNBTData();
            this.upgrade = upgrade == null ? null : upgrade.upgrade();
            invalidatePeripheral();
        }
    }

    public synchronized void updateValues(@Nullable IPocketUpgrade upgrade) {
        setLevel((ServerLevel) infectedBlockEntity.getLevel());
        setPosition(infectedBlockEntity.getBlockPos());
        if (this.upgrade != upgrade) {
            this.upgrade = upgrade;
            invalidatePeripheral();
        }
    }
}
