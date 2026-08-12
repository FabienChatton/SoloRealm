package ch.solorealm.beans.machine;

import ch.solorealm.ContextWrk;
import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientPair;
import ch.solorealm.beans.ingredient.IngredientType;

public class MiningMachine extends MachineNode {
    private final IngredientMaterial material;
    private final IngredientType type;
    public MiningMachine(IngredientMaterial material) {
        this(material, IngredientType.ORE);
    }

    public MiningMachine(IngredientMaterial material, IngredientType type) {
        super(EdgeIOSettings.OUTPUT, new IngredientPair(material, type));
        this.material = material;
        this.type = type;
    }

    @Override
    public String getAssetResourcePath() {
        return String.format("ingredients/%s_%s.png", material.toString().toLowerCase(), type.toString().toLowerCase());
    }

    @Override
    public String getMachineDisplayName() {
        String string = material.toString().toLowerCase();
        return String.format("%s Vein", ContextWrk.upperFirstLetter(string));
    }
}
