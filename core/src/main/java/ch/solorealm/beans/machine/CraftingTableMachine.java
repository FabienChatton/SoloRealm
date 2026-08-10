package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientPair;
import ch.solorealm.beans.ingredient.IngredientType;

public class CraftingTableMachine extends MachineNode {
    public CraftingTableMachine() {
        super(new EdgeIOSettings[]{EdgeIOSettings.INPUT_OUTPUT, EdgeIOSettings.INPUT},
            new MachineProcessRecipe(
                new IngredientPair(IngredientType.ORE, IngredientMaterial.COKE_OVEN_BRICK),

                new IngredientPair(IngredientType.RAW, IngredientMaterial.SAND),
                new IngredientPair(IngredientType.RAW, IngredientMaterial.CLAY)),
            new MachineProcessRecipe(
                new IngredientPair(IngredientType.ORE, IngredientMaterial.FIREBRICK),

                new IngredientPair(IngredientType.INGOT, IngredientMaterial.BRICK),
                new IngredientPair(IngredientType.RAW, IngredientMaterial.CLAY)
            )
        );
    }

    @Override
    public String getAssetRecourcePath() {
        return "machines/Crafting_Table.png";
    }

    @Override
    public String getMachineDisplayName() {
        return "Crafting Table";
    }
}
