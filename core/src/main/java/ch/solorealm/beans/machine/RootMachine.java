package ch.solorealm.beans.machine;

import ch.solorealm.beans.RootGrid;

public class RootMachine extends MachineNode {
    public final RootGrid rootGrid;
    public RootMachine(RootGrid rootGrid) {
        super(EdgeIOSettings.NONE);
        this.rootGrid = rootGrid;
    }

    @Override
    public String getMachineDisplayName() {
        return "root";
    }
}
