package ch.solorealm.beans.machine;

public class RootMachine extends MachineNode {
    public RootMachine() {
        super(null);
    }

    @Override
    public String getMachineName() {
        return "root";
    }
}
