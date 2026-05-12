package ch.solorealm.beans;

import ch.solorealm.beans.machine.RootMachine;

public class RootGrid {
    public final RootMachine[] rootNodes;

    public RootGrid(int nbrOfRoot) {
        this.rootNodes = new RootMachine[nbrOfRoot];
        for (int i = 0; i < nbrOfRoot; i++) {
            rootNodes[i] = new RootMachine();
        }
    }
}
