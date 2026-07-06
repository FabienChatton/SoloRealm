package ch.solorealm.beans.ingredient;

import java.util.function.Function;

public enum IngredientMaterial {
    COPPER(CopperIngredient::new),
    IRON(IronIngredient::new),
    ;

    public final Function<IngredientType, IngredientCard> ingredientConstructor;

    IngredientMaterial(Function<IngredientType, IngredientCard> ingredientConstructor) {
        this.ingredientConstructor = ingredientConstructor;
    }
}
