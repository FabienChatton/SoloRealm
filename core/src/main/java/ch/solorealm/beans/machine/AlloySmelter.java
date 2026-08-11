package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientPair;
import ch.solorealm.beans.ingredient.IngredientType;

public class AlloySmelter extends MachineNode {

    public AlloySmelter() {
        super(new EdgeIOSettings[]{EdgeIOSettings.INPUT_OUTPUT, EdgeIOSettings.INPUT},
            new MachineProcessRecipe(new IngredientPair(IngredientMaterial.BRONZE, IngredientType.INGOT),
                new IngredientPair(IngredientMaterial.TIN, IngredientType.INGOT), new IngredientPair(IngredientMaterial.COPPER, IngredientType.INGOT)));
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
