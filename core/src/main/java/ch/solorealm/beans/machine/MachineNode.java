package ch.solorealm.beans.machine;

import ch.solorealm.beans.GetAssetResource;

public abstract class MachineNode implements GetAssetResource {
    public final MachineEdge[] edges;

    public MachineNode(MachineEdge[] edges) {
        this.edges = edges;
    }

    public abstract String getMachineName();

    @Override
    public String getAssetName() {
        return String.format("machines/%s.png", getMachineName());
    }
}
