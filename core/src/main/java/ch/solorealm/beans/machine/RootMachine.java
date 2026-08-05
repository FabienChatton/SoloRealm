package ch.solorealm.beans.machine;

public class RootMachine extends MachineNode {
    public RootMachine() {
        super(EdgeIOSettings.NONE);
    }

    @Override
    public String getMachineDisplayName() {
        return "root";
    }
}
