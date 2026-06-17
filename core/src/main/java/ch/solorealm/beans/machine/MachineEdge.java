package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientCard;
import ch.solorealm.beans.ingredient.IngredientType;

public class MachineEdge {
    private IngredientCard input;
    private IngredientCard output;
    private MachineNode node;

    public final IngredientType inputType;
    public final IngredientType outputType;

    public MachineEdge(IngredientType inputType, IngredientType outputType) {
        this.inputType = inputType;
        this.outputType = outputType;
    }

    public void setNode(MachineNode node) {
        this.node = node;
    }

    public MachineNode getNode() {
        return node;
    }
}
