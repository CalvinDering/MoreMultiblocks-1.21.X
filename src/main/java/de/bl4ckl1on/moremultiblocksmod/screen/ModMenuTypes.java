package de.bl4ckl1on.moremultiblocksmod.screen;

import de.bl4ckl1on.moremultiblocksmod.MoreMultiblocksMod;
import de.bl4ckl1on.moremultiblocksmod.screen.block.InfuserMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MoreMultiblocksMod.MOD_ID);

    public static final Supplier<MenuType<InfuserMenu>> INFUSER_MENU = MENUS.register("infuser_menu", () -> IMenuTypeExtension.create(InfuserMenu::new));

    public static void register(IEventBus eventBus) { MENUS.register(eventBus); }

}
