package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientCard;
import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;
import ch.solorealm.beans.ingredient.IngredientTypeMaterialPair;

public class MachineEdge {
    private MachineNode node;
    private MachineNode childNode;
    private int edgeIndex;

    public final IngredientType inputType;
    public final IngredientType outputType;
    public IngredientCard input;
    public IngredientCard output;
    public IngredientMaterial exNihiloMaterial;

    public MachineEdge(IngredientType inputType, IngredientType outputType) {
        this.inputType = inputType;
        this.outputType = outputType;
    }

    public void setNode(MachineNode node) {
        this.node = node;
    }

    public void setEdgeIndex(int edgeIndex) {
        this.edgeIndex = edgeIndex;
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

    public boolean isDropValide(IngredientCard ingredientCard, boolean inputSlot) {
        if (!inputSlot) return false;
        if (input != null) return false;
        if (inputType != IngredientType._COMPLEX) {
            return ingredientCard.ingredientType == inputType;
        } else {
            for (IngredientTypeMaterialPair ingredientTypeMaterialPair : node.specialRecipe.input()) {
                if (ingredientCard.ingredientMaterial == ingredientTypeMaterialPair.material() &&
                    ingredientCard.ingredientType == ingredientTypeMaterialPair.type()) {
                    return true;
                }
            }
            return false;
        }
    }

    public void moveIngredientCard(IngredientCard ingredientCard, MachineEdge dstEdge, boolean dstInputSlot) {
        if (input == ingredientCard) {
            input = null;
        } else if (output == ingredientCard) {
            output = null;
        } else {
            System.err.println("Fail to move ingredient card. Can not find ingredient card "
                + ingredientCard + " in " + this + " edge " + " in " + getNode() + " machine");
            return;
        }
        if (dstInputSlot) {
            if (dstEdge.input != null) {
                System.err.println("Fail to move ingredient card. The destination edge input as already a ingredient");
                return;
            }
            dstEdge.input = ingredientCard;
        } else {
            if (dstEdge.output != null) {
                System.err.println("Fail to move ingredient card. The destination edge output as already a ingredient");
                return;
            }
            dstEdge.output = ingredientCard;
        }
        ingredientCard.edgeAttached = dstEdge;
    }

    public MachineEdge getDestinationParentEdge() {
        MachineEdge[] parentEdges = getNode().getParent().getNode().edges;
        for (MachineEdge parentEdge : parentEdges) {
            if (parentEdge.getChildNode() != null && parentEdge.getChildNode().edges[edgeIndex] == this) {
                return parentEdge;
            }
        }
        return null;
    }
}
