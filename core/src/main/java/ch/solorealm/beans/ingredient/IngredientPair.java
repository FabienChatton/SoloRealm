package ch.solorealm.beans.ingredient;

public record IngredientPair(IngredientType type, IngredientMaterial material) {
    @Override
    public String toString() {
        return String.format("%s %s", type, material);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IngredientPair(IngredientType type1, IngredientMaterial material1)) {
            return type1 == type && material1 == material;
        }
        return false;
    }
}
