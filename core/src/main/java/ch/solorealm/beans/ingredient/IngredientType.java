package ch.solorealm.beans.ingredient;

public enum IngredientType {
    ANY,
    /** Default type for miner. Ore turns into ingot in a furnace */
    ORE,
    INGOT,
    RAW,
    WIRE,
    PLATE,
    CABLE,
    ;

    public boolean isCompatible(IngredientType inputType) {
        if (this == ANY) return true;
        return this == inputType;
    }
}
