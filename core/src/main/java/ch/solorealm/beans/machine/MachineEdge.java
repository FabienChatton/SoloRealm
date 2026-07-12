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

    public boolean isDropValide(IngredientCard ingredientCard, boolean input) {
        if (input) {
            return ingredientCard.getIngredientType() == inputType;
        } else {
            return ingredientCard.getIngredientType() == outputType;
        }
    }

    public void addIngredientCard(IngredientCard ingredientCard, boolean inputSlot) {
        if (inputSlot) {
            this.input = ingredientCard;
        } else {
            this.output = ingredientCard;
        }
    }

    public void removeIngredientCard(boolean inputSlot) {
        if (inputSlot) {
            this.input = null;
        } else {
            this.output = null;
        }
    }
}
