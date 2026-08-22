package ch.solorealm.beans.levels;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;
import ch.solorealm.beans.machine.*;

public class Level4 extends LevelGenerator {
    public Level4() {
        super("Steal Age", "ingredients/steel_ingot.png",
            new MachineNode[]{
                new MiningMachine(IngredientMaterial.CLAY, IngredientType.RAW),
                new FurnaceMachine(),
                new CraftingTableMachine(),
            },
            new FoundationNode[]{
                new FoundationNode(IngredientType.INGOT, IngredientMaterial.FIREBRICK),
                new FoundationNode(IngredientType.RAW, IngredientMaterial.COAL_COKE),
                new FoundationNode(IngredientType.INGOT, IngredientMaterial.STEEL)
            },
            new ShopNode[]{
                new ShopNode(FurnaceMachine::new, 0),
                new ShopNode(() -> new MiningMachine(IngredientMaterial.COAL, IngredientType.ORE), 0),
                new ShopNode(CokeOvenMachine::new, 0),
                new ShopNode(BrickBlastFurnaceMachine::new, 1),
                new ShopNode(() -> new MiningMachine(IngredientMaterial.IRON, IngredientType.ORE), 1)
            });
    }
}
