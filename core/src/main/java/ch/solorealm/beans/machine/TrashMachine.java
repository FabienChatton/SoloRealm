package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientPair;
import ch.solorealm.beans.ingredient.IngredientType;

public class TrashMachine extends MachineNode {
    public TrashMachine() {
        super(EdgeIOSettings.INPUT, null, new IngredientPair(IngredientMaterial.ANY, IngredientType.ANY));
    }

    @Override
    public String getAssetRecourcePath() {
        return "machines/Trash.png";
    }

    @Override
    public String getMachineDisplayName() {
        return "Trash";
    }
}
