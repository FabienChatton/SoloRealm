package ch.solorealm.beans.levels;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;
import ch.solorealm.beans.machine.*;

public class Level8 extends LevelGenerator {
    public Level8() {
        super("Level 8", "ingredients/mixed_metal_ingot.png",
            new MachineNode[]{
                new MiningMachine(IngredientMaterial.COPPER),
                new MiningMachine(IngredientMaterial.RUBBER),
                new FurnaceMachine(),
                new WiremillMachine(),
                new CraftingTableMachine(),
            },
            new FoundationNode[]{
                new FoundationNode(IngredientType.CABLE, IngredientMaterial.COPPER),
                new FoundationNode(IngredientType.RAW, IngredientMaterial.MOTOR),
                new FoundationNode(IngredientType.RAW, IngredientMaterial.CONVEYOR),
            },
            new ShopNode[]{
                new ShopNode(FurnaceMachine::new, 0),
                new ShopNode(RodExtruderMachine::new, 0),
                new ShopNode(() -> new MiningMachine(IngredientMaterial.IRON), 0),

                new ShopNode(CraftingTableMachine::new, 1),
            });

    }
}
