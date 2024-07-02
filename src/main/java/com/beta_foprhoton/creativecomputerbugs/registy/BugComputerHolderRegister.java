package com.beta_foprhoton.creativecomputerbugs.registy;

import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.core.BugComputerHolder;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.upgrades.UpgradeData;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.world.level.block.Block.popResource;

public class BugComputerHolderRegister {
    private final Map<Integer , BugComputerHolder> registry;
    private int idCounter = 0;

    public BugComputerHolderRegister () {
        this.registry = new HashMap<>();
    }

    public BugComputerHolder getHolder (int ID) {
        return registry.get(ID);
    }
    
    public void tick () {
        registry.forEach((id, holder) -> {
            holder.serverTick();
        });
    }

    public BugComputerHolder create (BlockEntity infectBlockEntity, UpgradeData<IPocketUpgrade> upgrade, ComputerFamily family, Direction direction) {
        var holderOld = BugComputerHolder.getBugComputerHolder(infectBlockEntity);
        if (holderOld != null) return null;
        var holder = new BugComputerHolder(infectBlockEntity, CCBItems.BUG_WORM.asStack(), upgrade, family, direction, idCounter);
        registry.put(holder.id, holder);
        idCounter ++;
        return holder;
    }

    public void remove (int ID) {
        var holder = registry.get(ID);
        holder.unload();
        registry.remove(ID);
        popResource(holder.getLevel(), holder.getInfectedBlockEntity().getBlockPos(), holder.getBugItem());
    }
}