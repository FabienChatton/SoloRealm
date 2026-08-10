package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientPair;
import ch.solorealm.beans.ingredient.IngredientType;

public class FurnaceMachine extends MachineNode {
    public FurnaceMachine() {
        super(EdgeIOSettings.INPUT_OUTPUT,
            new MachineProcessRecipe(
                new IngredientPair(IngredientType.INGOT, IngredientMaterial.ANY),
                new IngredientPair(IngredientType.ORE, IngredientMaterial.ANY)),
            new MachineProcessRecipe(
                new IngredientPair(IngredientType.INGOT, IngredientMaterial.BRICK),
                new IngredientPair(IngredientType.RAW, IngredientMaterial.CLAY)
            ));
    }

    @Override
    public String getMachineDisplayName() {
        return "Furnace";
    }
}
