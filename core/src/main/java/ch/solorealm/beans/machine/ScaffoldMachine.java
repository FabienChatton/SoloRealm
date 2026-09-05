package ch.solorealm.beans.machine;

public class ScaffoldMachine extends MachineNode {
    public ScaffoldMachine() {
        super(EdgeIOSettings.NONE);
    }

    @Override
    public String getMachineDisplayName() {
        return "Scaffold";
    }
}
