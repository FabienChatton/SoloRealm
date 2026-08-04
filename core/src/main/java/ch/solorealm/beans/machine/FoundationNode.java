package ch.solorealm.beans.machine;

import ch.solorealm.ContextWrk;
import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;

public class FoundationNode extends MachineNode {
    public final IngredientType ingredientType;
    public final IngredientMaterial ingredientMaterial;

    public FoundationNode(IngredientType ingredientType, IngredientMaterial ingredientMaterial) {
        super(new MachineEdge[]{new MachineEdge(ingredientType, ingredientMaterial, null)});
        this.ingredientType = ingredientType;
        this.ingredientMaterial = ingredientMaterial;
    }

    @Override
    public String getAssetRecourcePath() {
        return String.format("ingredients/%s_%s.png", ingredientMaterial, ingredientType).toLowerCase();
    }

    @Override
    public String getMachineDisplayName() {
        return String.format("%s %s",
            ContextWrk.upperFirstLetter(ingredientMaterial.toString()),
            ContextWrk.upperFirstLetter(ingredientType.toString()));
    }
}
