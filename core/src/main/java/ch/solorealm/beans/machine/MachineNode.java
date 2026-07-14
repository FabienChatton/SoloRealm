package ch.solorealm.beans.machine;

import ch.solorealm.beans.ContextUi;
import ch.solorealm.beans.GetAssetResource;
import ch.solorealm.beans.ingredient.IngredientCard;

public abstract class MachineNode implements GetAssetResource {
    public final MachineEdge[] edges;
    private MachineEdge parent;

    public MachineNode(MachineEdge[] edges) {
        this.edges = edges;
        for (MachineEdge edge : edges) {
            edge.setNode(this);
        }
    }

    public void process(ContextUi contextUi) {
        // all edge input must be full
        // all edge output must be empty
        for (MachineEdge edge : edges) {
            if (edge.input == null) return;
            if (edge.output != null) return;
        }
        contextUi.clearActorIngredientCard(this);
        for (MachineEdge edge : edges) {
            if (edge.outputType == null) {
                edge.input = null;
                continue;
            }
            IngredientCard newCardTransformed = edge.input.ingredientMaterial.ingredientConstructor.apply(edge.outputType);
            newCardTransformed.edgeAttached = edge;
            contextUi.addActorIngredientCard(newCardTransformed, edge, false);
            edge.input = null;
            edge.output = newCardTransformed;
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
