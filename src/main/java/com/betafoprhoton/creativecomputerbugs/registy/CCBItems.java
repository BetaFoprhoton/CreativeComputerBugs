package com.betafoprhoton.creativecomputerbugs.registy;

import com.betafoprhoton.creativecomputerbugs.foundation.item.DebugStick;
import com.betafoprhoton.creativecomputerbugs.foundation.item.bugs.ParasiteItem;
import com.betafoprhoton.creativecomputerbugs.foundation.item.bugs.WormItem;
import com.simibubi.create.AllCreativeModeTabs;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;

import static com.betafoprhoton.creativecomputerbugs.CCBMain.MODID;
import static com.betafoprhoton.creativecomputerbugs.CCBMain.REGISTRATE;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCBItems {
    static {
        REGISTRATE.setCreativeTab(CCBCreativeModeTab.CCB_CREATIVE_TAB);
    }

    public static final ItemEntry<WormItem> BUG_WORM = REGISTRATE
            .item("bug_worm", WormItem::new)
            .register();

    public static final ItemEntry<ParasiteItem> BUG_PARASITE = REGISTRATE
            .item("bug_parasite", ParasiteItem::new)
            .register();

    public static final ItemEntry<DebugStick> DEBUG_STICK = REGISTRATE
            .item("debug_stick", DebugStick::new)
            .properties(p -> p.stacksTo(1))
            .model((ctx, provider) ->
                provider.generated(ctx)
                        .override()
                        .predicate(new ResourceLocation(MODID, "is_active"), 0.0f)
                        .model(provider.withExistingParent(ctx.getName() + "_0", "item/generated"))
                        .end()
                        .override()
                        .predicate(new ResourceLocation(MODID, "is_active"), 1.0f)
                        .model(provider.withExistingParent(ctx.getName() + "_1", "item/generated"))
                        .end()
            )
            .onRegister(item -> {
                final var key = new ResourceLocation(MODID,  "is_active");
                ItemProperties.register(item, key, (itemStack, clientLevel, livingEntity, seed) -> {
                    final var tag = itemStack.getOrCreateTag();
                    if (tag.getBoolean("isActive")) return 1.0f;
                    else return  0.0f;
                });
            })
            .register();

    public static void register() {}

}
