package com.beta_foprhoton.creativecomputerbugs.foundation.item.bugs;

import com.beta_foprhoton.creativecomputerbugs.CCBMain;
import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.block.AllBlockAPIs;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public class BugWormItem extends AbstractBugItem {
    public BugWormItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
        if (blockEntity == null) return super.useOn(context);
        if (AllBlockAPIs.getSuitableAPI(blockEntity) != null) {
            setUpgrade(context.getItemInHand(), null);
            boolean flag = CCBMain.BUG_COMPUTER_HOLDER_REGISTER.create(blockEntity, AbstractBugItem.getUpgradeWithData(context.getItemInHand()), family, context.getClickedFace()) == null;
            if (flag) return super.useOn(context);
            context.getItemInHand().shrink(1);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }
}
