package ch.solorealm.beans.ingredient;

public class CopperIngredient extends IngredientCard {
    public CopperIngredient(IngredientType ingredientType) {
        super(IngredientMaterial.COPPER, ingredientType);
    }

    public void setType(IngredientType type) {
        ingredientType = type;
    }
}
