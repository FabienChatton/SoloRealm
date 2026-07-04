package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientType;

public class AssemblingMachine extends MachineNode {
    public AssemblingMachine() {
        super(new MachineEdge[]{
            new MachineEdge(IngredientType.INGOT, IngredientType.INGOT),
            new MachineEdge(IngredientType.INGOT, null)
        });
    }


    @Override
    public String getAssetRecourcePath() {
        return "machines/Assembling_Machine.png";
    }

    @Override
    public String getMachineDisplayName() {
        return "Assembling Machine";
    }
}
