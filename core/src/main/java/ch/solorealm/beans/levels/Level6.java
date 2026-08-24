package ch.solorealm.beans.levels;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;
import ch.solorealm.beans.machine.*;

public class Level6 extends LevelGenerator {
    public Level6() {
        super("Easier Cable", "machines/Wiremill.png",
            new MachineNode[]{
                new MiningMachine(IngredientMaterial.COPPER),
                new FurnaceMachine(),
                new WiremillMachine(),
            },
            new FoundationNode[]{
                new FoundationNode(IngredientType.WIRE, IngredientMaterial.COPPER),
                new FoundationNode(IngredientType.PLATE, IngredientMaterial.RUBBER),
                new FoundationNode(IngredientType.CABLE, IngredientMaterial.COPPER),
            },
            new ShopNode[]{
                new ShopNode(() -> new MiningMachine(IngredientMaterial.RUBBER), 0),
                new ShopNode(BendingMachine::new, 0),

                new ShopNode(CraftingTableMachine::new, 1),
                new ShopNode(FurnaceMachine::new, 1)
            });
    }
}
