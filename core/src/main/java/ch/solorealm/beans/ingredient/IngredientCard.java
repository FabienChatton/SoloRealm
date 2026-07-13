package ch.solorealm.beans.ingredient;

import ch.solorealm.beans.GetAssetResource;
import ch.solorealm.beans.machine.MachineEdge;

public abstract class IngredientCard implements GetAssetResource {
    private final IngredientMaterial ingredientMaterial;
    protected IngredientType ingredientType;
    public MachineEdge edgeAttached;

    public IngredientCard(IngredientMaterial ingredientMaterial, IngredientType ingredientType) {
        this.ingredientType = ingredientType;
        this.ingredientMaterial = ingredientMaterial;
    }

    @Override
    public String getAssetRecourcePath() {
        return String.format("ingredients/%s_%s.png", ingredientMaterial.toString(), ingredientType.toString()).toLowerCase();
    }

    public IngredientType getIngredientType() {
        return ingredientType;
    }
}
