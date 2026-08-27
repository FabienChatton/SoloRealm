package ch.solorealm.beans;

import ch.solorealm.beans.machine.MachineEdge;
import ch.solorealm.beans.machine.MachineNode;
import ch.solorealm.beans.machine.RootMachine;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RootGrid {
    public final RootMachine[] rootNodes;

    public RootGrid(int nbrOfRoot) {
        this.rootNodes = new RootMachine[nbrOfRoot];
        for (int i = 0; i < nbrOfRoot; i++) {
            rootNodes[i] = new RootMachine(this);
        }
    }

    public boolean isDropValide(MachineNode machineFuturParent, int dropActorIndex, MachineNode machineToDrop) {
        if (machineFuturParent == machineToDrop) return false;
        int edgePos = getEdgePos(machineFuturParent.edges[dropActorIndex]);
        int cardWith = getTotalWidth(machineToDrop);
        if (edgePos + cardWith > rootNodes.length) return false;
        int height = 0;
        MachineNode rootParent = machineFuturParent;
        while(rootParent != null) {
            if (rootParent.getParent() == null) {
                break;
            }
            rootParent = rootParent.getParent().getNode();
            height++;
        }

        RootGrid dstRootGrid = ((RootMachine) rootParent).rootGrid;
        List<MachineNode> machineToIgnore = machineToDrop.getAllChildren();
        while (machineToDrop != null) {
            MachineNode[] machinesAtHeight = new MachineNode[dstRootGrid.rootNodes.length];
            for (RootMachine rootNode : dstRootGrid.rootNodes) {
                getMachineAtHeight(rootNode, machinesAtHeight, height + 1);
            }

            for (int i = 0; i <= edgePos + machineToDrop.edges.length - 1 && i < machinesAtHeight.length; i++) {
                if (machinesAtHeight[i] != null) {
                    if (machineToIgnore.contains(machinesAtHeight[i])) continue;
                    if (machinesAtHeight[i].edges.length + i - 1 >= edgePos) {
                        return false;
                    }
                }
            }
            machineToDrop = machineToDrop.edges[0].getChildNode();
            height++;
        }
        return true;
    }

    private void getMachineAtHeight(MachineNode machineNode, MachineNode[] machinesAtHeight, int height) {
        if (machineNode == null) {
            return;
        }
        if (height == 0) {
            machinesAtHeight[getEdgePos(machineNode.edges[0])] = machineNode;
        }
        for (int i = 0; i < machineNode.edges.length; i++) {
            MachineEdge edge = machineNode.edges[i];
            getMachineAtHeight(edge.getChildNode(), machinesAtHeight, height - 1);
        }
    }

    private int getEdgePos(MachineEdge edge) {
        int pos = 0;
        MachineNode node = edge.getNode();
        while (node.getParent() != null) {
            for (int i = 0; i < node.edges.length; i++) {
                if (node.edges[i] == edge) {
                    pos += i;
                }
            }
            edge = node.getParent();
            if (edge != null) {
                node = edge.getNode();
            }
        }
        for (int i = 0; i < rootNodes.length; i++) {
            if (rootNodes[i] == node) {
                pos += i;
            }
        }
        return pos;
    }

    private int getTotalWidth(MachineNode node) {
        if (node == null) {
            return 1;
        }

        int max = 1;
        for (int i = 0; i < node.edges.length; i++) {
            max = Math.max(max, getTotalWidth(node.edges[i].getChildNode()) + i);
        }
        return max;
    }

    public void process(ContextUi contextUi) {
        Queue<MachineNode> machineQueue = new LinkedList<>();
        for (RootMachine rootNode : rootNodes) {
            if (rootNode.edges[0].getChildNode() != null) {
                machineQueue.add(rootNode.edges[0].getChildNode());
            }
            while (!machineQueue.isEmpty()) {
                MachineNode machineNode = machineQueue.poll();
                machineNode.process(contextUi);
                for (MachineEdge edge : machineNode.edges) {
                    if (edge.getChildNode() != null) {
                        machineQueue.add(edge.getChildNode());
                    }
                }
            }
        }
    }
}
