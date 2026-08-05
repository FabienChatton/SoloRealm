package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientPair;

public record MachineProcessRecipe(IngredientPair output, IngredientPair... input) { }
