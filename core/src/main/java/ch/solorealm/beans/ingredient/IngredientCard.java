package ch.solorealm.beans.ingredient;

import ch.solorealm.beans.GetAssetResource;
import ch.solorealm.beans.machine.MachineEdge;

public class IngredientCard implements GetAssetResource {
    public final IngredientMaterial ingredientMaterial;
    public final IngredientType ingredientType;
    public MachineEdge edgeAttached;

    public IngredientCard(IngredientMaterial ingredientMaterial, IngredientType ingredientType) {
        if (ingredientMaterial == IngredientMaterial.ANY) throw new IllegalArgumentException("IngredientMaterial cannot be any for a card");
        if (ingredientType == IngredientType.ANY) throw new IllegalArgumentException("IngredientType cannot be any for a card");
        this.ingredientType = ingredientType;
        this.ingredientMaterial = ingredientMaterial;
    }

    @Override
    public String getAssetResourcePath() {
        return getAssetResourcePath(ingredientMaterial, ingredientType);
    }

    public static String getAssetResourcePath(IngredientMaterial material, IngredientType type) {
        return String.format("ingredients/%s_%s.png", material, type).toLowerCase();
    }

}
