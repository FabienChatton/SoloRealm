package ch.solorealm.beans.ingredient;

import ch.solorealm.beans.GetAssetResource;

public abstract class IngredientCard implements GetAssetResource {
    private final IngredientMaterial ingredientMaterial;
    protected IngredientType ingredientType;

    public IngredientCard(IngredientMaterial ingredientMaterial, IngredientType ingredientType) {
        this.ingredientType = ingredientType;
        this.ingredientMaterial = ingredientMaterial;
    }

    @Override
    public String getAssetName() {
        return String.format("ingredients/%s_%s.png", ingredientMaterial.toString(), ingredientType.toString()).toLowerCase();
    }
}
