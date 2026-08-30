package ch.solorealm.beans.ingredient;

public enum IngredientMaterial {
    ANY,
    COPPER,
    IRON,
    TIN,
    BRONZE,
    SAND,
    CLAY,
    WOOD,
    CHARCOAL,
    COKE_OVEN_BRICK,
    COAL_COKE,
    BRICK,
    COAL,
    FIREBRICK,
    STEEL,
    RUBBER,
    MIXED_METAL,
    MOTOR,
    CONVEYOR,
    ;

    public boolean isCompatible(IngredientMaterial inputMaterial) {
        if (this == ANY) return true;
        return this == inputMaterial;
    }
}
