package ch.solorealm.beans;

public abstract class IngredientCard {
    private final IngredientMaterial ingredientMaterial;
    protected IngredientType ingredientType;

    public IngredientCard(IngredientMaterial ingredientMaterial, IngredientType ingredientType) {
        this.ingredientType = ingredientType;
        this.ingredientMaterial = ingredientMaterial;
    }

    public String getAssetName() {
        return String.format("ingredients/%s_%s.png", ingredientMaterial.toString(), ingredientType.toString()).toLowerCase();
    }
}
