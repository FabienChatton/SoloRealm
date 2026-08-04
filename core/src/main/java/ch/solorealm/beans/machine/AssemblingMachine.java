package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;

public class AssemblingMachine extends MachineNode {
    public AssemblingMachine() {
        super(new MachineEdge[]{
            new MachineEdge(IngredientType.INGOT, IngredientMaterial.ANY,IngredientType.INGOT),
            new MachineEdge(IngredientType.INGOT, IngredientMaterial.ANY,null)
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
