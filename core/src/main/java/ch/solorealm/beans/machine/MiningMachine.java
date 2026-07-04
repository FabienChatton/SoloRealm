package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientType;

public class MiningMachine extends MachineNode {

    public MiningMachine() {
        super(new MachineEdge[]{new MachineEdge(null, IngredientType.ORE)});
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
