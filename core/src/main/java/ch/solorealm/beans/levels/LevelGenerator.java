package ch.solorealm.beans.levels;

import ch.solorealm.beans.machine.FoundationNode;
import ch.solorealm.beans.machine.MachineNode;

public abstract class LevelGenerator {
    public final String title;
    public final String iconTexture;
    public final MachineNode[] initialTableau;
    public final FoundationNode[] initialFoundation;
    public final ShopNode[] initialShop;

    public LevelGenerator(String title, String iconTexture, MachineNode[] initialTableau, FoundationNode[] initialFoundation, ShopNode[] initialShop) {
        this.title = title;
        this.iconTexture = iconTexture;
        this.initialTableau = initialTableau;
        this.initialFoundation = initialFoundation;
        this.initialShop = initialShop;
    }
}
