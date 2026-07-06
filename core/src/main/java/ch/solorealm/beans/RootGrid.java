package ch.solorealm.beans;

import ch.solorealm.beans.machine.MachineEdge;
import ch.solorealm.beans.machine.MachineNode;
import ch.solorealm.beans.machine.RootMachine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RootGrid {
    public final RootMachine[] rootNodes;

    public RootGrid(int nbrOfRoot) {
        this.rootNodes = new RootMachine[nbrOfRoot];
        for (int i = 0; i < nbrOfRoot; i++) {
            rootNodes[i] = new RootMachine();
        }
    }

    public boolean isDropValide(int rootIndex, MachineNode machine) {
        int cardWidth = getTotalWidth(machine);
        for (int i = rootIndex; i < rootIndex + cardWidth; i++) {
            if (i >= rootNodes.length) {
                return false;
            }
            for (int j = 0; j < rootNodes[i].edges.length; j++) {
                if (rootNodes[i].edges[j].getChildNode() == machine) {
                    continue;
                }
                if (rootNodes[i].edges[j].getChildNode() != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isDropValide(MachineNode machine, int dropActorIndex, MachineNode machineToDrop) {
        if (machine == machineToDrop) return false;
        int edgePos = getEdgePos(machine.edges[dropActorIndex]);
        int cardWith = getTotalWidth(machineToDrop);
        if (edgePos + cardWith > rootNodes.length) return false;
        int height = 0;
        MachineNode rootParent = machine;
        while(rootParent != null) {
            if (rootParent.getParent() == null) {
                break;
            }
            rootParent = rootParent.getParent().getNode();
            height++;
        }

        List<MachineNode> machineAtHeight = getMachineAtHeight(rootParent, height + 1, cardWith);
        loop:
        for (int i = 0; i < machineAtHeight.size() && i < cardWith; i++) {
            MachineNode machineNode = machineAtHeight.get(i);
            if (machineNode == machineToDrop) continue;
            if (machineNode.getParent().getNode() == machine) {
                for (int j = 0; j < machine.edges.length; j++) {
                    if (j < dropActorIndex) continue loop;
                }
            }
            return false;
        }
        return true;
    }

    private List<MachineNode> getMachineAtHeight(MachineNode machineNode, int height, int cardWith) {
        if (machineNode == null) {
            return List.of();
        }
        if (height == 0) {
            return List.of(machineNode);
        }
        List<MachineNode> list = new ArrayList<>();
        for (int i = 0; i < machineNode.edges.length && i < cardWith; i++) {
            MachineEdge edge = machineNode.edges[i];
            list.addAll(getMachineAtHeight(edge.getChildNode(), height - 1, cardWith - i));
        }
        return list;
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
