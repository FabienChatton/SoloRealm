package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientPair;
import ch.solorealm.beans.ingredient.IngredientType;

public class CokeOvenMachine extends MachineNode {
    public CokeOvenMachine() {
        super(EdgeIOSettings.INPUT_OUTPUT,
            new MachineProcessRecipe(
                new IngredientPair(IngredientMaterial.CHARCOAL, IngredientType.RAW),
                new IngredientPair(IngredientMaterial.WOOD, IngredientType.RAW)),
            new MachineProcessRecipe(
                new IngredientPair(IngredientMaterial.COAL_COKE, IngredientType.RAW),
                new IngredientPair(IngredientMaterial.COAL, IngredientType.INGOT))
        );
    }

    @Override
    public String getAssetResourcePath() {
        return "machines/Coke_Oven.png";
    }

    @Override
    public String getMachineDisplayName() {
        return "Coke Oven";
    }
}
