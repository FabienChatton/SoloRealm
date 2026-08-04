package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;

public class AlloySmelter extends MachineNode {

    public AlloySmelter() {
        super(new MachineEdge[]{
            new MachineEdge(IngredientType.INGOT, IngredientMaterial.COPPER, IngredientType.INGOT, IngredientMaterial.BRONZE),
            new MachineEdge(IngredientType.INGOT, IngredientMaterial.TIN, null)
        });
    }

    @Override
    public String getMachineDisplayName() {
        return "Alloy Smelter";
    }

    @Override
    public String getAssetRecourcePath() {
        return "machines/Alloy_Smelter.png";
    }
}
