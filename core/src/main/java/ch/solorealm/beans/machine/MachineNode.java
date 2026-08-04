package ch.solorealm.beans.machine;

import ch.solorealm.beans.ContextUi;
import ch.solorealm.beans.GetAssetResource;
import ch.solorealm.beans.ingredient.IngredientCard;
import ch.solorealm.beans.ingredient.IngredientMaterial;

public abstract class MachineNode implements GetAssetResource {
    public final MachineEdge[] edges;
    private MachineEdge parent;

    public MachineNode(MachineEdge[] edges) {
        if (edges.length == 0) throw new IllegalArgumentException(String.format("\"%s\" must have at least one edge", getClass().getName()));
        this.edges = edges;
        postInit();
    }

    private void postInit() {
        for (int i = 0; i < edges.length; i++) {
            MachineEdge edge = edges[i];
            edge.setNode(this);
            edge.setEdgeIndex(i);
        }
    }

    public void process(ContextUi contextUi) {
        // move output to destination parent input
        for (MachineEdge edge : edges) {
            IngredientCard cardToMove = edge.output;
            if (cardToMove != null) {
                MachineEdge destinationParentEdge = edge.getDestinationParentEdge();
                if (destinationParentEdge != null && destinationParentEdge.isDropValide(cardToMove, true)) {
                    contextUi.moveActorIngredientCard(cardToMove, edge, destinationParentEdge, true);
                }
            }
        }


        // all edge input must be full
        // all edge output must be empty
        for (MachineEdge edge : edges) {
            if (edge.input == null && edge.inputType != null && edge.inputMaterial != null) return;
            if (edge.output != null) return;
        }
        contextUi.clearActorIngredientCard(this);
        for (MachineEdge edge : edges) {
            // process "craft"
            if (edge.outputType == null) {
                edge.input = null;
                continue;
            }
            IngredientMaterial material;
            if (edge.outputMaterial != IngredientMaterial.ANY) {
                material = edge.outputMaterial;
            } else {
                material = edge.input.ingredientMaterial;
            }
            IngredientCard newCardTransformed = new IngredientCard(material, edge.outputType);
            newCardTransformed.edgeAttached = edge;
            edge.input = null;
            edge.output = newCardTransformed;
            contextUi.addActorIngredientCard(newCardTransformed, edge, false);
        }
    }

    public abstract String getMachineDisplayName();

    @Override
    public String getAssetRecourcePath() {
        return String.format("machines/%s.png", getMachineDisplayName());
    }

    public void setParent(MachineEdge machineNode) {
        parent = machineNode;
    }

    public MachineEdge getParent() {
        return parent;
    }
}
