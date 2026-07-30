package ch.solorealm.beans.levels;

import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientType;
import ch.solorealm.beans.machine.AlloySmelter;
import ch.solorealm.beans.machine.FoundationNode;
import ch.solorealm.beans.machine.FurnaceMachine;
import ch.solorealm.beans.machine.MiningMachine;

public class Level1 extends LevelGenerator {
    public Level1() {
        super(new ActorCardData[]{
            new ActorCardData(new MiningMachine(IngredientMaterial.COPPER), 0),
            new ActorCardData(new MiningMachine(IngredientMaterial.TIN), 1)
        }, new FoundationNode[]{
            new FoundationNode(IngredientType.ORE, IngredientMaterial.COPPER),
            new FoundationNode(IngredientType.INGOT, IngredientMaterial.COPPER),
            new FoundationNode(IngredientType.INGOT, IngredientMaterial.BRONZE)
        }, new ActorCardData[]{
            new ActorCardData(new FurnaceMachine(), 0),
            new ActorCardData(new AlloySmelter(), 1)
        });
    }
}
