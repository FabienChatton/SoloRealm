package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;

public class FurnaceMachine extends MachineNode {
    public FurnaceMachine() {
        super(new MachineEdge[]{new MachineEdge(IngredientType.ORE, IngredientMaterial.ANY, IngredientType.INGOT)});
    }

    @Override
    public String getMachineDisplayName() {
        return "Furnace";
    }
}
