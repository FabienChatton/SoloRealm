package ch.solorealm.beans;

import ch.solorealm.beans.ingredient.IngredientCard;
import ch.solorealm.beans.machine.MachineEdge;

public interface ContextUi {
    void addActorIngredientCard(IngredientCard ingredientCard, MachineEdge edge, boolean inputSlot);
}
