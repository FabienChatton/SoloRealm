package ch.solorealm.beans.levels;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;
import ch.solorealm.beans.machine.*;

public class Level2 extends LevelGenerator {
    public Level2() {
        super(new TableauNode[]{
            new TableauNode(new MiningMachine(IngredientMaterial.SAND, IngredientType.RAW), 0),
            new TableauNode(new MiningMachine(IngredientMaterial.CLAY, IngredientType.RAW), 1),
            new TableauNode(new CraftingTableMachine(), 2)
        }, new FoundationNode[] {
            new FoundationNode(IngredientType.ORE, IngredientMaterial.COKE_OVEN_BRICK),
            new FoundationNode(IngredientType.INGOT, IngredientMaterial.COKE_OVEN_BRICK),
            new FoundationNode(IngredientType.RAW, IngredientMaterial.CHARCOAL)
        }, new ShopNode[]{
            new ShopNode(FurnaceMachine::new, 0),
            new ShopNode(CokeOvenMachine::new, 1), new ShopNode(() -> new MiningMachine(IngredientMaterial.WOOD, IngredientType.RAW), 1)
        });
    }
}
