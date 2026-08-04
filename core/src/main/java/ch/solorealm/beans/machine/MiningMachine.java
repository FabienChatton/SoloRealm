package ch.solorealm.beans.machine;

import ch.solorealm.ContextWrk;
import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;

public class MiningMachine extends MachineNode {
    private final IngredientMaterial material;
    public MiningMachine(IngredientMaterial material) {
        super(new MachineEdge[]{new MachineEdge(null, null, IngredientType.ORE, material)});
        this.material = material;
    }

    @Override
    public String getAssetRecourcePath() {
        return String.format("ingredients/%s_ore.png", material.toString().toLowerCase());
    }

    @Override
    public String getMachineDisplayName() {
        String string = material.toString().toLowerCase();
        return String.format("%s Vein", ContextWrk.upperFirstLetter(string));
    }
}
