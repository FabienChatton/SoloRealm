package ch.solorealm.beans.machine;

import ch.solorealm.beans.ContextUi;
import ch.solorealm.beans.GetAssetResource;
import ch.solorealm.beans.ingredient.IngredientCard;
import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientSpecialRecipe;
import ch.solorealm.beans.ingredient.IngredientType;

public abstract class MachineNode implements GetAssetResource {
    public final MachineEdge[] edges;
    public final IngredientSpecialRecipe specialRecipe;
    private MachineEdge parent;

    public MachineNode(MachineEdge[] edges) {
        if (edges.length == 0) throw new IllegalArgumentException(String.format("\"%s\" must have at least one edge", getClass().getName()));
        this.edges = edges;
        this.specialRecipe = null;
        postInit();
    }

    public MachineNode(IngredientSpecialRecipe specialRecipe) {
        if (specialRecipe.input().length == 0) throw new IllegalArgumentException(String.format("\"%s\" must have at least one input. Output recipe: %s", getClass().getName(), specialRecipe.output()));
        this.specialRecipe = specialRecipe;
        edges = new MachineEdge[specialRecipe.input().length];
        if (specialRecipe.output() == null) {
            edges[0] = new MachineEdge(IngredientType._COMPLEX, null);
        } else {
            edges[0] = new MachineEdge(IngredientType._COMPLEX, IngredientType._COMPLEX);
        }
        int nInput = specialRecipe.input().length;
        for (int i = 1; i < nInput; i++) {
            edges[i] = new MachineEdge(IngredientType._COMPLEX, null);
        }
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
            if (edge.input == null && edge.exNihiloMaterial == null) return;
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
            IngredientType type;
            if (edge.input == null) {
                material = edge.exNihiloMaterial;
                type = edge.outputType;
            } else if (edge.inputType == IngredientType._COMPLEX) {
                material = specialRecipe.output().material();
                type = specialRecipe.output().type();
            } else {
                material = edge.input.ingredientMaterial;
                type = edge.outputType;
            }
            IngredientCard newCardTransformed = material.ingredientConstructor.apply(type);
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
