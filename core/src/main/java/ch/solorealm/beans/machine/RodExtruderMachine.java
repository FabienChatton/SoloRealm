package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientPair;
import ch.solorealm.beans.ingredient.IngredientType;

public class RodExtruderMachine extends MachineNode {
    public RodExtruderMachine() {
        super(EdgeIOSettings.INPUT_OUTPUT,
            new IngredientPair(IngredientMaterial.ANY, IngredientType.ROD),
            new IngredientPair(IngredientMaterial.ANY, IngredientType.INGOT));
    }

    @Override
    public String getAssetResourcePath() {
        return "machines/Rod_Extruder.png";
    }

    @Override
    public String getMachineDisplayName() {
        return "Rod Extruder";
    }
}
