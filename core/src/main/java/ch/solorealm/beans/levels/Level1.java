package ch.solorealm.beans.levels;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;
import ch.solorealm.beans.machine.AlloySmelter;
import ch.solorealm.beans.machine.FoundationNode;
import ch.solorealm.beans.machine.FurnaceMachine;
import ch.solorealm.beans.machine.MiningMachine;

public class Level1 extends LevelGenerator {
    public Level1() {
        super("Level 1", "ingredients/bronze_ingot.png", new TableauNode[]{
            new TableauNode(new MiningMachine(IngredientMaterial.COPPER), 0),
            new TableauNode(new MiningMachine(IngredientMaterial.TIN), 1)
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
