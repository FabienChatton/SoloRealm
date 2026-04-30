package ch.solorealm.beans;

public class CopperIngredient extends IngredientCard {
    public CopperIngredient(IngredientType ingredientType) {
        super(IngredientMaterial.COPPER, ingredientType);
    }

    public void setType(IngredientType type) {
        ingredientType = type;
    }
}
