package ch.solorealm.beans.levels;

import ch.solorealm.beans.machine.FoundationNode;

public abstract class LevelGenerator {
    public final TableauNode[] initialTableau;
    public final FoundationNode[] initialFoundation;
    public final ShopNode[] initialShop;

    public LevelGenerator(TableauNode[] initialTableau, FoundationNode[] initialFoundation, ShopNode[] initialShop) {
        this.initialTableau = initialTableau;
        this.initialFoundation = initialFoundation;
        this.initialShop = initialShop;
    }
}
