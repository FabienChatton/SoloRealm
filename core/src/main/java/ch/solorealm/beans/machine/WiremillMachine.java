package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientPair;
import ch.solorealm.beans.ingredient.IngredientType;

public class WiremillMachine extends MachineNode {

    public WiremillMachine() {
        super(EdgeIOSettings.INPUT_OUTPUT,
            new MachineProcessRecipe(
                new IngredientPair(IngredientMaterial.COPPER, IngredientType.WIRE),
                new IngredientPair(IngredientMaterial.COPPER, IngredientType.INGOT)
            ));
    }

    @Override
    public String getMachineDisplayName() {
        return "Wiremill";
    }
}
