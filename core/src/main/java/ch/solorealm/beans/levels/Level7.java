package ch.solorealm.beans.levels;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;
import ch.solorealm.beans.machine.*;

public class Level7 extends LevelGenerator {
    public Level7() {
        super("Mixed Metal", "ingredients/mixed_metal_ingot.png",
            new MachineNode[]{
                new MiningMachine(IngredientMaterial.IRON),
                new MiningMachine(IngredientMaterial.COAL),
                new FurnaceMachine(),
                new CokeOvenMachine(),
                new BrickBlastFurnaceMachine(),
            },
            new FoundationNode[]{
                new FoundationNode(IngredientType.INGOT, IngredientMaterial.STEEL),
                new FoundationNode(IngredientType.INGOT, IngredientMaterial.COPPER),
                new FoundationNode(IngredientType.PLATE, IngredientMaterial.MIXED_METAL),
            },
            new ShopNode[]{
                new ShopNode(FurnaceMachine::new, 0),
                new ShopNode(() -> new MiningMachine(IngredientMaterial.COPPER), 0),

                new ShopNode(CraftingTableMachine::new, 1),
                new ShopNode(BendingMachine::new, 1),
            });
    }
}
