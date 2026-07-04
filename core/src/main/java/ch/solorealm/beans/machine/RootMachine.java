package ch.solorealm.beans.machine;

public class RootMachine extends MachineNode {
    public RootMachine() {
        super(new MachineEdge[] {
            new MachineEdge(null, null)
        });
    }

    @Override
    public String getMachineDisplayName() {
        return "root";
    }
}
