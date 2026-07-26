package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientSpecialRecipe;
import ch.solorealm.beans.ingredient.IngredientType;
import ch.solorealm.beans.ingredient.IngredientTypeMaterialPair;

public class AlloySmelter extends MachineNode {

    public AlloySmelter() {
        super(new IngredientSpecialRecipe(
            new IngredientTypeMaterialPair(IngredientType.INGOT, IngredientMaterial.IRON),
            new IngredientTypeMaterialPair(IngredientType.INGOT, IngredientMaterial.COPPER),
            new IngredientTypeMaterialPair(IngredientType.INGOT, IngredientMaterial.COPPER)
        ));
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
