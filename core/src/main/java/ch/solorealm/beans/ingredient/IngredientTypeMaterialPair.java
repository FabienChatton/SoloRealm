package ch.solorealm.beans.ingredient;

public record IngredientTypeMaterialPair(IngredientType type, IngredientMaterial material) {
    @Override
    public String toString() {
        return String.format("%s %s", type, material);
    }
}
