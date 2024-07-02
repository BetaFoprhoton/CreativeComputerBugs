package com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.core;

import com.beta_foprhoton.creativecomputerbugs.CCBMain;
import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.block.BlockAPITypes;
import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.block.create.CreativeMotorAPI;
import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.computer.blocks.IBugComputerHolder;
import com.google.common.base.Strings;
import com.simibubi.create.content.kinetics.motor.CreativeMotorBlockEntity;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.upgrades.UpgradeData;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.impl.BundledRedstone;
import dan200.computercraft.shared.ModRegistry;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.core.ServerContext;
import dan200.computercraft.shared.computer.inventory.ComputerMenuWithoutInventory;
import dan200.computercraft.shared.config.Config;
import dan200.computercraft.shared.util.BlockEntityHelpers;
import dan200.computercraft.shared.util.DirectionUtil;
import dan200.computercraft.shared.util.IDAssigner;
import dan200.computercraft.shared.util.RedstoneUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.LockCode;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

import static com.beta_foprhoton.creativecomputerbugs.foundation.item.bugs.AbstractBugItem.INFECTED_BLOCK_FLAG;
import static com.beta_foprhoton.creativecomputerbugs.foundation.item.bugs.AbstractBugItem.getUpgrade;

public class BugComputerHolder implements IBugComputerHolder, Nameable, MenuProvider {
    private int instanceID = -1;
    private int computerID = -1;
    public final int id;
    protected @javax.annotation.Nullable String label = null;
    private boolean on = false;
    boolean startOn = false;
    private boolean fresh = false;
    private final BlockEntity infectedBlockEntity;
    private final ItemStack bugItem;
    @Nullable
    private UpgradeData<IPocketUpgrade> upgrade;

    private int invalidSides = 0;

    private LockCode lockCode = LockCode.NO_LOCK;

    private final ComputerFamily family;
    private final Direction positiveDirection;

    public BugComputerHolder(@NotNull BlockEntity infectedBlockEntity, ItemStack bugItem, @Nullable UpgradeData<IPocketUpgrade> upgrade, ComputerFamily family, Direction clickDirection, int id) {
        this.infectedBlockEntity = infectedBlockEntity;
        this.bugItem = bugItem;
        this.upgrade = upgrade;
        this.family = family;
        this.positiveDirection = clickDirection;
        this.id = id;
        infectedBlockEntity.getPersistentData().putInt(INFECTED_BLOCK_FLAG, id);
    }

    public void unload() {
        if (getLevel().isClientSide) return;
        var computer = getServerComputer();
        if (computer != null) computer.close();
        infectedBlockEntity.getPersistentData().remove(INFECTED_BLOCK_FLAG);
        instanceID = -1;
    }


    public boolean isUsable(Player player) {
        return BaseContainerBlockEntity.canUnlock(player, lockCode, getDisplayName())
                && BlockEntityHelpers.isUsable(infectedBlockEntity, player, getInteractRange());
    }

    protected boolean canNameWithTag(Player player) {
        return false;
    }

    protected double getInteractRange() {
        return BlockEntityHelpers.DEFAULT_INTERACT_RANGE;
    }

    public void serverTick() {
        if (getLevel().isClientSide) return;
        if (computerID < 0 && !startOn) return; // Don't tick if we don't need a computer!
        if (infectedBlockEntity.isRemoved())
            CCBMain.BUG_COMPUTER_HOLDER_REGISTER.remove(id);

        var computer = createServerComputer();

        IPocketUpgrade upgrade1 = null;
        if (upgrade != null) {
            upgrade1 = upgrade.upgrade();
        }

        if (startOn || (fresh && on)) {
            computer.turnOn();
            startOn = false;
        }

        computer.keepAlive();

        fresh = false;
        computerID = computer.getID();
        label = computer.getLabel();
        on = computer.isOn();

        if (upgrade1 != null)
            upgrade1.update((IPocketAccess) computer, computer.getPeripheral(ComputerSide.BACK));


        // TODO: This should ideally be split up into label/id/on (which should save NBT and sync to client) and
        //  redstone (which should update outputs)
        if (computer.hasOutputChanged()) updateOutput();
    }


    protected boolean isPeripheralBlockedOnSide(ComputerSide localSide) {
        return false;
    }

    protected Direction getDirection() {
        return positiveDirection;
    }

    protected ComputerSide remapToLocalSide(Direction globalSide) {
        return remapLocalSide(DirectionUtil.toLocal(getDirection(), globalSide));
    }

    protected ComputerSide remapLocalSide(ComputerSide localSide) {
        return localSide;
    }

    private void updateRedstoneInputs(ServerComputer computer) {
        var pos = infectedBlockEntity.getBlockPos();
        for (var dir : DirectionUtil.FACINGS) updateRedstoneInput(computer, dir, pos.relative(dir));
    }

    private void updateRedstoneInput(ServerComputer computer, Direction dir, BlockPos targetPos) {
        var offsetSide = dir.getOpposite();
        var localDir = remapToLocalSide(dir);

        computer.setRedstoneInput(localDir, RedstoneUtil.getRedstoneInput(infectedBlockEntity.getLevel(), targetPos, dir));
        computer.setBundledRedstoneInput(localDir, BundledRedstone.getOutput(getLevel(), targetPos, offsetSide));
    }

    public void updateInputsImmediately() {
        var computer = getServerComputer();
        if (computer != null) updateInputsImmediately(computer);
    }

    private void updateInputsImmediately(ServerComputer computer) {
        var pos = infectedBlockEntity.getBlockPos();
        for (var dir : DirectionUtil.FACINGS) {
            updateRedstoneInput(computer, dir, pos.relative(dir));
        }
    }

    private void updateInputAt(BlockPos neighbour) {
        var computer = getServerComputer();
        if (computer == null) return;

        for (var dir : DirectionUtil.FACINGS) {
            var offset = infectedBlockEntity.getBlockPos().relative(dir);
            if (offset.equals(neighbour)) {
                updateRedstoneInput(computer, dir, offset);
                invalidSides |= 1 << dir.ordinal();
                return;
            }
        }

        // If the position is not any adjacent one, update all inputs. This is pretty terrible, but some redstone mods
        // handle this incorrectly.
        updateRedstoneInputs(computer);
        invalidSides = (1 << 6) - 1; // Mark all peripherals as dirty.
    }

    /**
     * Update the block's state and propagate redstone output.
     */
    public void updateOutput() {
        for (var dir : DirectionUtil.FACINGS) RedstoneUtil.propagateRedstoneOutput(getLevel(), infectedBlockEntity.getBlockPos(), dir);

        var computer = getServerComputer();
        if (computer != null) updateRedstoneInputs(computer);
    }

    protected InfectedBlockServerComputer createComputer(int id) {
        var family = getFamily();
        return new InfectedBlockServerComputer(
                infectedBlockEntity, id, label,
                family, Config.computerTermWidth,
                Config.computerTermHeight, bugItem
        );
    }

    @Override
    public final int getComputerID() {
        return computerID;
    }

    @Override
    public final @javax.annotation.Nullable String getLabel() {
        return label;
    }

    @Override
    public final void setComputerID(int id) {
        if (getLevel().isClientSide || computerID == id) return;
        computerID = id;
        setChanged();
    }

    @Override
    public final void setLabel(@javax.annotation.Nullable String label) {
        if (getLevel().isClientSide || Objects.equals(this.label, label)) return;
        this.label = label;
        var computer = getServerComputer();
        if (computer != null) computer.setLabel(label);
        setChanged();
    }

    public void setChanged () {
        infectedBlockEntity.setChanged();
    }

    @Override
    public ComputerFamily getFamily() {
        return family;
    }

    public final ServerComputer createServerComputer() {
        var server = getLevel().getServer();
        if (server == null) throw new IllegalStateException("Cannot access server computer on the client.");

        var changed = false;

        var computer = ServerContext.get(server).registry().get(instanceID);
        if (computer == null) {
            if (computerID < 0) {
                computerID = ComputerCraftAPI.createUniqueNumberedSaveDir(server, IDAssigner.COMPUTER);
                BlockEntityHelpers.updateBlock(infectedBlockEntity);
            }

            computer = createComputer(computerID);
            instanceID = computer.register();
            fresh = true;
            changed = true;

            var upgrade = getUpgrade(bugItem);

            ((InfectedBlockServerComputer) computer).updateValues(upgrade);
            try {
                computer.addAPI(BlockAPITypes.getSuitableAPI(infectedBlockEntity).getDeclaredConstructor(BlockEntity.class).newInstance(infectedBlockEntity));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        infectedBlockEntity.getPersistentData().contains(INFECTED_BLOCK_FLAG);

        if (changed) updateInputsImmediately(computer);
        return computer;
    }

    @Nullable
    public ServerComputer getServerComputer() {
        return getLevel().isClientSide || getLevel().getServer() == null ? null : ServerContext.get(getLevel().getServer()).registry().get(instanceID);
    }

    public Level getLevel () {
        return infectedBlockEntity.getLevel();
    }

    // Networking stuff

    /*
    protected void transferStateFrom(BugComputerHolder copy) {
        if (copy.computerID != computerID || copy.instanceID != instanceID) {
            unload();
            instanceID = copy.instanceID;
            computerID = copy.computerID;
            label = copy.label;
            on = copy.on;
            startOn = copy.startOn;
            lockCode = copy.lockCode;
            BlockEntityHelpers.updateBlock(infectedBlockEntity);
        }
        copy.instanceID = -1;
    }
    */
    @Override
    public @NotNull Component getName() {
        if (label != null) {
            return Component.literal(label);
        }
        return Component.empty();
    }

    @Override
    public boolean hasCustomName() {
        return !Strings.isNullOrEmpty(label);
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return hasCustomName() ? Component.literal(label) : null;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Nameable.super.getDisplayName() == null ? Component.empty() : Nameable.super.getDisplayName();
    }

    @Nullable
    public static BugComputerHolder getBugComputerHolder(BlockEntity blockEntity) {
        if (blockEntity.getPersistentData().contains(INFECTED_BLOCK_FLAG))
            return CCBMain.BUG_COMPUTER_HOLDER_REGISTER.getHolder(blockEntity.getPersistentData().getInt(INFECTED_BLOCK_FLAG));
        return null;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new ComputerMenuWithoutInventory(ModRegistry.Menus.COMPUTER.get(), id, inventory, this::isUsable, createServerComputer(), getFamily());
    }

    public ItemStack getBugItem() {
        return bugItem;
    }

    public BlockEntity getInfectedBlockEntity() {
        return infectedBlockEntity;
    }
}
