package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientPair;
import ch.solorealm.beans.ingredient.IngredientType;

public class CokeOvenMachine extends MachineNode {
    public CokeOvenMachine() {
        super(EdgeIOSettings.INPUT_OUTPUT,
            new MachineProcessRecipe(
                new IngredientPair(IngredientType.RAW, IngredientMaterial.CHARCOAL),
                new IngredientPair(IngredientType.RAW, IngredientMaterial.WOOD)),
            new MachineProcessRecipe(
                new IngredientPair(IngredientType.RAW, IngredientMaterial.COAL_COKE),
                new IngredientPair(IngredientType.INGOT, IngredientMaterial.COAL))
        );
    }

    @Override
    public String getAssetRecourcePath() {
        return "machines/Coke_Oven.png";
    }

    @Override
    public String getMachineDisplayName() {
        return "Coke Oven";
    }
}
