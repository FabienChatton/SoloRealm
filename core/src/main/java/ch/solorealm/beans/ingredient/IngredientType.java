package ch.solorealm.beans.ingredient;

public enum IngredientType {
    ORE,
    INGOT,
    ANY,
    ;

    public boolean isCompatible(IngredientType inputType) {
        if (this == ANY) return true;
        return this == inputType;
    }
}
