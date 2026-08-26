package ch.solorealm.beans.machine;

import ch.solorealm.beans.ContextUi;
import ch.solorealm.beans.GetAssetResource;
import ch.solorealm.beans.ingredient.IngredientCard;
import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientPair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class MachineNode implements GetAssetResource {
    public final MachineEdge[] edges;
    public final List<MachineProcessRecipe> machineProcessRecipes;
    private MachineEdge parent;

    public MachineNode(EdgeIOSettings[] edgeIOSettings, MachineProcessRecipe... machineProcessRecipes) {
        if (edgeIOSettings.length == 0) throw new IllegalArgumentException(String.format("\"%s\" must have at least one edge", getClass().getName()));
        edges = new MachineEdge[edgeIOSettings.length];
        for (int i = 0; i < edgeIOSettings.length; i++) {
            edges[i] = new MachineEdge(edgeIOSettings[i]);
        }
        this.machineProcessRecipes = List.of(machineProcessRecipes);
        postInit();
    }

    public MachineNode(EdgeIOSettings edgeIOSettings, MachineProcessRecipe... machineProcessRecipes) {
        this(new EdgeIOSettings[]{edgeIOSettings}, machineProcessRecipes);
    }

    public MachineNode(EdgeIOSettings edgeIOSettings, IngredientPair output, IngredientPair... input) {
        this(edgeIOSettings, new MachineProcessRecipe(output, input));
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
        // all edge output must be empty
        for (MachineEdge edge : edges) {
            if (edge.output != null) return;
        }

        MachineProcessRecipe valideProcessRecipe = null;
        for (MachineProcessRecipe machineProcessRecipe : machineProcessRecipes) {
            if (isProcessRecipeFull(machineProcessRecipe)) {
                valideProcessRecipe = machineProcessRecipe;
                break;
            }
        }
        if (valideProcessRecipe == null) {
            return;
        }

        contextUi.clearActorIngredientCard(this);
        for (MachineEdge edge : edges) {
            // process "craft"
            if (edge.edgeIOSettings == EdgeIOSettings.INPUT) {
                edge.input = null;
                continue;
            }
            IngredientMaterial material;
            if (valideProcessRecipe.output().material() != IngredientMaterial.ANY) {
                material = valideProcessRecipe.output().material();
            } else {
                material = edge.input.ingredientMaterial;
            }
            IngredientCard newCardTransformed = new IngredientCard(material, valideProcessRecipe.output().type());
            newCardTransformed.edgeAttached = edge;
            edge.input = null;
            edge.output = newCardTransformed;
            contextUi.addActorIngredientCard(newCardTransformed, edge, false);
        }
    }

    public List<MachineProcessRecipe> getAvailableProcessRecipe() {
        List<MachineProcessRecipe> ret = new ArrayList<>(machineProcessRecipes);
        for (MachineProcessRecipe machineProcessRecipe : machineProcessRecipes) {
            boolean valideRecipe = false;
            for (IngredientPair ingredientInput : machineProcessRecipe.input()) {
                for (MachineEdge edge : edges) {
                    if (edge.input != null) {
                        if (ingredientInput.type().isCompatible(edge.input.ingredientType) && ingredientInput.material().isCompatible(edge.input.ingredientMaterial)) {
                            valideRecipe = true;
                        }
                    } else {
                        valideRecipe = true;
                    }
                }
                if (!valideRecipe) {
                    ret.remove(machineProcessRecipe);
                }
            }
        }
        return ret;
    }

    public boolean isProcessRecipeFull(MachineProcessRecipe processRecipe) {
        edge:
        for (MachineEdge edge : edges) {
            if (edge.input == null && edge.edgeIOSettings != EdgeIOSettings.OUTPUT) {
                return false;
            }
            if (processRecipe.input().length == 0) {
                return true;
            }
            for (IngredientPair ingredientPair : processRecipe.input()) {
                if (ingredientPair.material().isCompatible(edge.input.ingredientMaterial) && ingredientPair.type().isCompatible(edge.input.ingredientType)) {
                    continue edge;
                }
            }
            return false;
        }
        return true;
    }

    public boolean isValideProcessRecipe(IngredientCard card) {
        List<MachineProcessRecipe> availableProcessRecipe = getAvailableProcessRecipe();
        Set<IngredientPair> ingredientPairs = getRemainingIngredient(availableProcessRecipe);
        for (IngredientPair ingredientPair : ingredientPairs) {
            if (ingredientPair.material().isCompatible(card.ingredientMaterial) && ingredientPair.type().isCompatible(card.ingredientType)) {
                return true;
            }
        }
        return false;
    }

    private Set<IngredientPair> getRemainingIngredient(List<MachineProcessRecipe> availableProcessRecipe) {
        Set<IngredientPair> ret = new HashSet<>();
        for (MachineProcessRecipe machineProcessRecipe : availableProcessRecipe) {
            List<IngredientPair> tmp = new ArrayList<>(List.of(machineProcessRecipe.input()));
            edges:
            for (MachineEdge edge : edges) {
                for (IngredientPair ingredientPair : machineProcessRecipe.input()) {
                    if (edge.input != null) {
                        if (ingredientPair.type().isCompatible(edge.input.ingredientType) && ingredientPair.material().isCompatible(edge.input.ingredientMaterial)) {
                            tmp.remove(ingredientPair);
                            continue edges;
                        }
                    }
                }
            }
            ret.addAll(tmp);
        }
        return ret;
    }


    public List<MachineNode> getAllChildren() {
        List<MachineNode> children = new ArrayList<>();
        children.add(this);
        for (MachineEdge edge : edges) {
            if (edge.getChildNode() != null) {
                children.addAll(edge.getChildNode().getAllChildren());
            }
        }
        return children;
    }

    public abstract String getMachineDisplayName();

    @Override
    public String getAssetResourcePath() {
        return String.format("machines/%s.png", getMachineDisplayName());
    }

    public void setParent(MachineEdge machineNode) {
        parent = machineNode;
    }

    public MachineEdge getParent() {
        return parent;
    }
}
