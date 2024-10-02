package de.bl4ckl1on.moremultiblocksmod.item;

import de.bl4ckl1on.moremultiblocksmod.MoreMultiblocksMod;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MoreMultiblocksMod.MOD_ID);

    public static final DeferredItem<Item> ENERGONITE = ITEMS.register("energonite",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ZYTHERIUM = ITEMS.register("zytherium",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
