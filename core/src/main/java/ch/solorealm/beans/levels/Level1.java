package ch.solorealm.beans.levels;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;
import ch.solorealm.beans.machine.*;

public class Level1 extends LevelGenerator {
    public Level1() {
        super("Bronze Age", "ingredients/bronze_ingot.png",
            new MachineNode[]{
                new MiningMachine(IngredientMaterial.COPPER),
                new MiningMachine(IngredientMaterial.TIN)
            }, new FoundationNode[]{
                new FoundationNode(IngredientType.ORE, IngredientMaterial.COPPER),
                new FoundationNode(IngredientType.INGOT, IngredientMaterial.COPPER),
                new FoundationNode(IngredientType.INGOT, IngredientMaterial.BRONZE)
            }, new ShopNode[]{
                new ShopNode(FurnaceMachine::new, 0),
                new ShopNode(AlloySmelter::new, 1)
            });
    }
}
