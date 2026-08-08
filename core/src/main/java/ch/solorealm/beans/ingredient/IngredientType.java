package ch.solorealm.beans.ingredient;

public enum IngredientType {
    ANY,
    ORE,
    INGOT,
    RAW,
    ;

    public boolean isCompatible(IngredientType inputType) {
        if (this == ANY) return true;
        return this == inputType;
    }
}
