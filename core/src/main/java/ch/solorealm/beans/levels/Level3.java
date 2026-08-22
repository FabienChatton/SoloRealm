package ch.solorealm.beans.levels;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;
import ch.solorealm.beans.machine.*;

public class Level3 extends LevelGenerator {
    public Level3() {
        super("Into Fire", "ingredients/firebrick_ingot.png",
            new MachineNode[]{
                new MiningMachine(IngredientMaterial.CLAY, IngredientType.RAW),
                new FurnaceMachine(),
            }, new FoundationNode[]{
                new FoundationNode(IngredientType.INGOT, IngredientMaterial.BRICK),
                new FoundationNode(IngredientType.ORE, IngredientMaterial.FIREBRICK),
                new FoundationNode(IngredientType.INGOT, IngredientMaterial.FIREBRICK)
            }, new ShopNode[]{
                new ShopNode(CraftingTableMachine::new, 0),
                new ShopNode(() -> new MiningMachine(IngredientMaterial.CLAY, IngredientType.RAW), 0),
                new ShopNode(FurnaceMachine::new, 1)
            });
    }
}
