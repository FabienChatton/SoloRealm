package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientPair;
import ch.solorealm.beans.ingredient.IngredientType;

public class FurnaceMachine extends MachineNode {
    public FurnaceMachine() {
        super(EdgeIOSettings.INPUT_OUTPUT,
            new MachineProcessRecipe(
                new IngredientPair(IngredientMaterial.ANY, IngredientType.INGOT),
                new IngredientPair(IngredientMaterial.ANY, IngredientType.ORE)),
            new MachineProcessRecipe(
                new IngredientPair(IngredientMaterial.BRICK, IngredientType.INGOT),
                new IngredientPair(IngredientMaterial.CLAY, IngredientType.RAW)
            ));
    }

    @Override
    public String getMachineDisplayName() {
        return "Furnace";
    }
}
