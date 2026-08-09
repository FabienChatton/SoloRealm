package ch.solorealm.beans.levels;

import ch.solorealm.beans.machine.FoundationNode;

public abstract class LevelGenerator {
    public final String title;
    public final String iconTexture;
    public final TableauNode[] initialTableau;
    public final FoundationNode[] initialFoundation;
    public final ShopNode[] initialShop;

    public LevelGenerator(String title, String iconTexture, TableauNode[] initialTableau, FoundationNode[] initialFoundation, ShopNode[] initialShop) {
        this.title = title;
        this.iconTexture = iconTexture;
        this.initialTableau = initialTableau;
        this.initialFoundation = initialFoundation;
        this.initialShop = initialShop;
    }
}
