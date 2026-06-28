package ch.solorealm.actors;

import ch.solorealm.beans.RootGrid;
import ch.solorealm.beans.machine.MachineEdge;
import ch.solorealm.beans.machine.MachineNode;
import ch.solorealm.beans.machine.RootMachine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.List;

public class RootGridActor extends Table {
    public final RootGrid data;
    public final RootActor[] rootActors;

    public RootGridActor(RootGrid data, Skin skin, Texture backgroundTextureRoot) {
        this.data = data;
        HorizontalGroup hGroup = new HorizontalGroup();
        hGroup.space(32);
        rootActors = new RootActor[data.rootNodes.length];
        for (int i = 0; i < data.rootNodes.length; i++) {
            RootActor rootActor = new RootActor(skin, new RootMachine(), backgroundTextureRoot);
            hGroup.addActor(rootActor);
            rootActors[i] = rootActor;
        }
        add(hGroup);
    }

    public boolean isDropValide(int rootActorIndex, ActorMachineCard machineCard) {
        int cardWith = machineCard.data.edges.length;
        for (int i = rootActorIndex; i < rootActorIndex + cardWith; i++) {
            if (i >= rootActors.length) {
                return false;
            }
            if (!rootActors[i].getCardChildren().contains(machineCard)
                && !rootActors[i].getCardChildren().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isDropValide(ActorMachineCard card, int dropActorIndex, ActorMachineCard cardToDrop) {
        if (card == cardToDrop) return false;
        int cardWith = cardToDrop.data.edges.length;
        int height = 0;
        MachineNode rootParent = card.data;
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
            if (machineNode == cardToDrop.data) continue;
            if (machineNode.getParent().getNode() == card.data) {
                for (int j = 0; j < card.data.edges.length; j++) {
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
}
