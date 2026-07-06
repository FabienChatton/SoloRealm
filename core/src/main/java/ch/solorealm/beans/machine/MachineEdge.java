package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientCard;
import ch.solorealm.beans.ingredient.IngredientType;

public class MachineEdge {
    private MachineNode node;
    private MachineNode childNode;

    public final IngredientType inputType;
    public final IngredientType outputType;
    public IngredientCard input;
    public IngredientCard output;

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

    public MachineNode getChildNode() {
        return childNode;
    }

    public void setChildNode(MachineNode childNode) {
        this.childNode = childNode;
    }
}
