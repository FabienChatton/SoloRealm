package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;

public class MiningMachine extends MachineNode {

    public MiningMachine(IngredientMaterial material) {
        super(new MachineEdge[]{new MachineEdge(null, IngredientType.ORE)});
        edges[0].exNihiloMaterial = material;
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
