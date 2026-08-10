package ch.solorealm.beans.levels;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;
import ch.solorealm.beans.machine.CraftingTableMachine;
import ch.solorealm.beans.machine.FoundationNode;
import ch.solorealm.beans.machine.FurnaceMachine;
import ch.solorealm.beans.machine.MiningMachine;

public class Level3 extends LevelGenerator {
    public Level3() {
        super("Level 3", "ingredients/firebrick_ingot.png",
            new TableauNode[]{
                new TableauNode(new MiningMachine(IngredientMaterial.CLAY, IngredientType.RAW), 0),
                new TableauNode(new FurnaceMachine(), 1),
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
