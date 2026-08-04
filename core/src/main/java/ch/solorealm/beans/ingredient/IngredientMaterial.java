package ch.solorealm.beans.ingredient;

public enum IngredientMaterial {
    ANY,
    COPPER,
    IRON,
    TIN,
    BRONZE,
    ;

    public boolean isCompatible(IngredientMaterial inputMaterial) {
        if (this == ANY) return true;
        return this == inputMaterial;
    }
}
