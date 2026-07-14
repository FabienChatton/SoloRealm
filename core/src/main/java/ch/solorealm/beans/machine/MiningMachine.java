package ch.solorealm.beans.machine;

import ch.solorealm.beans.ContextUi;
import ch.solorealm.beans.ingredient.IngredientCard;
import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;

public class MiningMachine extends MachineNode {
    private final IngredientMaterial material;

    public MiningMachine(IngredientMaterial material) {
        super(new MachineEdge[]{new MachineEdge(null, IngredientType.ORE)});
        this.material = material;
    }

    @Override
    public void process(ContextUi contextUi) {
        MachineEdge edge = edges[0];
        if (edge.output == null) {
            IngredientCard ingredient = material.ingredientConstructor.apply(IngredientType.ORE);
            ingredient.edgeAttached = edge;
            edge.output = ingredient;
            contextUi.addActorIngredientCard(ingredient, edge, false);
        }
    }

    @Override
    public String getAssetRecourcePath() {
        return "machines/Assembling_Machine.png";
    }

    @Override
    public String getMachineDisplayName() {
        return "Miner";
    }
}
