package ch.solorealm.beans.machine;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientPair;
import ch.solorealm.beans.ingredient.IngredientType;

public class BrickBlastFurnaceMachine extends MachineNode {

    public BrickBlastFurnaceMachine() {
        super(new EdgeIOSettings[]{EdgeIOSettings.INPUT_OUTPUT, EdgeIOSettings.INPUT},
            new MachineProcessRecipe(
                new IngredientPair(IngredientMaterial.STEEL, IngredientType.INGOT),
                new IngredientPair(IngredientMaterial.IRON, IngredientType.INGOT),
                new IngredientPair(IngredientMaterial.COAL_COKE, IngredientType.RAW)));
    }

    @Override
    public String getAssetResourcePath() {
        return "machines/Brick_blast_furnace.png";
    }

    @Override
    public String getMachineDisplayName() {
        return "Brick Blast Furnace";
    }
}
