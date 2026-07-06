package ch.solorealm.beans.machine;

import ch.solorealm.beans.ContextUi;
import ch.solorealm.beans.GetAssetResource;

public abstract class MachineNode implements GetAssetResource {
    public final MachineEdge[] edges;
    private MachineEdge parent;

    public MachineNode(MachineEdge[] edges) {
        this.edges = edges;
        for (MachineEdge edge : edges) {
            edge.setNode(this);
        }
    }

    public void process(ContextUi contextUi) {

    }

    public abstract String getMachineDisplayName();

    @Override
    public String getAssetRecourcePath() {
        return String.format("machines/%s.png", getMachineDisplayName());
    }

    public void setParent(MachineEdge machineNode) {
        parent = machineNode;
    }

    public MachineEdge getParent() {
        return parent;
    }
}
