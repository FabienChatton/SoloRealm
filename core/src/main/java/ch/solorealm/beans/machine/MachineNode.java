package ch.solorealm.beans.machine;

import ch.solorealm.beans.ContextUi;
import ch.solorealm.beans.GetAssetResource;
import ch.solorealm.beans.ingredient.IngredientCard;
import ch.solorealm.beans.ingredient.IngredientMaterial;

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
            if (edge.input == null && edge.exNihiloMaterial == null) return;
            if (edge.output != null) return;
        }
        contextUi.clearActorIngredientCard(this);
        for (MachineEdge edge : edges) {
            if (edge.outputType == null) {
                edge.input = null;
                continue;
            }
            IngredientMaterial material;
            if (edge.input == null) {
                material = edge.exNihiloMaterial;
            } else {
                material = edge.input.ingredientMaterial;
            }
            IngredientCard newCardTransformed = material.ingredientConstructor.apply(edge.outputType);
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
