package ch.solorealm.beans;

import ch.solorealm.beans.ingredient.IngredientCard;
import ch.solorealm.beans.machine.MachineEdge;
import ch.solorealm.beans.machine.MachineNode;

public interface ContextUi {
    void addActorIngredientCard(IngredientCard ingredientCard, MachineEdge edge, boolean inputSlot);
    void clearActorIngredientCard(MachineNode machineNode);
}
