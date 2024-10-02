package de.bl4ckl1on.moremultiblocksmod.datagen;

import de.bl4ckl1on.moremultiblocksmod.block.ModBlocks;
import de.bl4ckl1on.moremultiblocksmod.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ENERGONITE_BLOCK.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', ModItems.ENERGONITE.get())
                .unlockedBy("has_energonite", has(ModItems.ENERGONITE)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ENERGONITE.get(), 9)
                .requires(ModBlocks.ENERGONITE_BLOCK)
                .unlockedBy("has_energonite", has(ModBlocks.ENERGONITE_BLOCK)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ZYTHERIUM_BLOCK.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', ModItems.ZYTHERIUM.get())
                .unlockedBy("has_zytherium", has(ModItems.ZYTHERIUM)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ZYTHERIUM.get(), 9)
                .requires(ModBlocks.ZYTHERIUM_BLOCK)
                .unlockedBy("has_zytherium", has(ModBlocks.ZYTHERIUM_BLOCK)).save(recipeOutput);

    }
}
