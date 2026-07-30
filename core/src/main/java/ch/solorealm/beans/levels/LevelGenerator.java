package ch.solorealm.beans.levels;

import ch.solorealm.beans.machine.FoundationNode;

public abstract class LevelGenerator {
    public final ActorCardData[] initialTableau;
    public final FoundationNode[] initialFoundation;
    public final ActorCardData[] initialShop;

    public LevelGenerator(ActorCardData[] initialTableau, FoundationNode[] initialFoundation, ActorCardData[] initialShop) {
        this.initialTableau = initialTableau;
        this.initialFoundation = initialFoundation;
        this.initialShop = initialShop;
    }
}
