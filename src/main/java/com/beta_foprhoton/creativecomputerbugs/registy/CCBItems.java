package com.beta_foprhoton.creativecomputerbugs.registy;

import com.beta_foprhoton.creativecomputerbugs.foundation.item.DebugStick;
import com.beta_foprhoton.creativecomputerbugs.foundation.item.bugs.BlockBugWormItem;
import com.simibubi.create.AllCreativeModeTabs;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraftforge.fml.common.Mod;

import static com.beta_foprhoton.creativecomputerbugs.CCBMain.REGISTRATE;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCBItems {
    static {
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);
    }

    public static final ItemEntry<BlockBugWormItem> BUG_WORM = REGISTRATE
            .item("bug_worm", BlockBugWormItem::new)
            .register();

    public static final ItemEntry<DebugStick> DEBUG_STICK = REGISTRATE
            .item("debug_stick", DebugStick::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static void register() {}

}
