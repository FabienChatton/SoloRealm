package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientType;

public class FurnaceMachine extends MachineNode {
    public FurnaceMachine() {
        super(new MachineEdge[]{new MachineEdge(IngredientType.ORE, IngredientType.INGOT)});
    }

    @Override
    public String getMachineDisplayName() {
        return "furnace";
    }
}
