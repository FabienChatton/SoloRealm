package ch.solorealm.beans.levels;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;
import ch.solorealm.beans.machine.*;

public class Level2 extends LevelGenerator {
    public Level2() {
        super("Carlos's Coke", "machines/Coke_Oven.png",
            new MachineNode[]{
                new MiningMachine(IngredientMaterial.SAND, IngredientType.RAW),
                new MiningMachine(IngredientMaterial.CLAY, IngredientType.RAW),
                new CraftingTableMachine(),
            }, new FoundationNode[] {
                new FoundationNode(IngredientType.ORE, IngredientMaterial.COKE_OVEN_BRICK),
                new FoundationNode(IngredientType.INGOT, IngredientMaterial.COKE_OVEN_BRICK),
                new FoundationNode(IngredientType.RAW, IngredientMaterial.COAL_COKE)
            }, new ShopNode[]{
                new ShopNode(FurnaceMachine::new, 0),
                new ShopNode(CokeOvenMachine::new, 1),
                new ShopNode(() -> new MiningMachine(IngredientMaterial.COAL, IngredientType.ORE), 1)
            });
    }
}
