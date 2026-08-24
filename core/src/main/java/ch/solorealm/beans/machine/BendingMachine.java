package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientPair;
import ch.solorealm.beans.ingredient.IngredientType;

public class BendingMachine extends MachineNode {
    public BendingMachine() {
        super(EdgeIOSettings.INPUT_OUTPUT,
            new MachineProcessRecipe(
                new IngredientPair(IngredientMaterial.RUBBER, IngredientType.PLATE),
                new IngredientPair(IngredientMaterial.RUBBER, IngredientType.INGOT)
            ));
    }

    @Override
    public String getAssetResourcePath() {
        return "machines/Bending_Machine.png";
    }

    @Override
    public String getMachineDisplayName() {
        return "Bending Machine";
    }
}
