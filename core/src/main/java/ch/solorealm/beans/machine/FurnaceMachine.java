package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientType;

public class FurnaceMachine extends MachineNode {
    public FurnaceMachine() {
        super(IngredientType.ORE, IngredientType.INGOT);
    }

    @Override
    protected String getMachineName() {
        return "furnace";
    }
}
