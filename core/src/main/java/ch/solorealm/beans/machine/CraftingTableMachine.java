package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientPair;
import ch.solorealm.beans.ingredient.IngredientType;

public class CraftingTableMachine extends MachineNode {
    public CraftingTableMachine() {
        super(new EdgeIOSettings[]{EdgeIOSettings.INPUT_OUTPUT, EdgeIOSettings.INPUT},
            new MachineProcessRecipe(
                new IngredientPair(IngredientMaterial.COKE_OVEN_BRICK, IngredientType.ORE),

                new IngredientPair(IngredientMaterial.SAND, IngredientType.RAW),
                new IngredientPair(IngredientMaterial.CLAY, IngredientType.RAW)),
            new MachineProcessRecipe(
                new IngredientPair(IngredientMaterial.FIREBRICK, IngredientType.ORE),

                new IngredientPair(IngredientMaterial.BRICK, IngredientType.INGOT),
                new IngredientPair(IngredientMaterial.CLAY, IngredientType.RAW)
            )
        );
    }

    @Override
    public String getAssetResourcePath() {
        return "machines/Crafting_Table.png";
    }

    @Override
    public String getMachineDisplayName() {
        return "Crafting Table";
    }
}
