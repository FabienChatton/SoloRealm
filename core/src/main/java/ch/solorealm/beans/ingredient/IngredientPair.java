package ch.solorealm.beans.ingredient;

public record IngredientPair(IngredientMaterial material, IngredientType type) {
    @Override
    public String toString() {
        return formatString(material, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IngredientPair(IngredientMaterial material1, IngredientType type1)) {
            return type1 == type && material1 == material;
        }
        return false;
    }

    public static String formatString(IngredientMaterial material, IngredientType type) {
        return String.format("%s %s", material, type);
    }

    public static String formatString(IngredientCard card) {
        return formatString(card.ingredientMaterial, card.ingredientType);
    }
}
