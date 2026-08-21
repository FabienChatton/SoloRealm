package ch.solorealm.beans.levels;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;
import ch.solorealm.beans.machine.CraftingTableMachine;
import ch.solorealm.beans.machine.FoundationNode;
import ch.solorealm.beans.machine.FurnaceMachine;
import ch.solorealm.beans.machine.MiningMachine;

public class Level5 extends LevelGenerator {
    public Level5() {
        super("Level 5", "ingredients/copper_cable.png",
            new TableauNode[]{
                new TableauNode(new CraftingTableMachine(), 0),
                new TableauNode(new MiningMachine(IngredientMaterial.COPPER), 2),
                new TableauNode(new FurnaceMachine(), 3),
                new TableauNode(new MiningMachine(IngredientMaterial.RUBBER), 4),
            },
            new FoundationNode[]{
                new FoundationNode(IngredientType.WIRE, IngredientMaterial.COPPER),
                new FoundationNode(IngredientType.PLATE, IngredientMaterial.RUBBER),
                new FoundationNode(IngredientType.CABLE, IngredientMaterial.COPPER)
            },
            new ShopNode[]{
                new ShopNode(CraftingTableMachine::new, 0),
                new ShopNode(FurnaceMachine::new, 0),

                new ShopNode(() -> new MiningMachine(IngredientMaterial.COPPER), 1),
                new ShopNode(() -> new MiningMachine(IngredientMaterial.RUBBER), 1),
            });
    }
}
