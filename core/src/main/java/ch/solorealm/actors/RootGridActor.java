package ch.solorealm.actors;

import ch.solorealm.beans.RootGrid;
import ch.solorealm.beans.machine.MachineEdge;
import ch.solorealm.beans.machine.MachineNode;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.LinkedList;
import java.util.Queue;

public class RootGridActor extends Table {
    public final RootGrid data;
    public final RootActor[] rootActors;

    public RootGridActor(RootGrid data, Skin skin, Texture backgroundTextureRoot) {
        this.data = data;
        HorizontalGroup hGroup = new HorizontalGroup();
        hGroup.space(32);
        rootActors = new RootActor[data.rootNodes.length];
        for (int i = 0; i < data.rootNodes.length; i++) {
            RootActor rootActor = new RootActor(data.rootNodes[i], backgroundTextureRoot);
            hGroup.addActor(rootActor);
            rootActors[i] = rootActor;
        }
        add(hGroup);
    }

    public void updateActorSimple() {
        for (RootActor rootActor : rootActors) {
            for (ActorMachineCard cardChild : rootActor.getCardChildren()) {
                cardChild.updateCardPos();
            }
        }
    }

    public void updateActorDeep() {
        Queue<ActorMachineCard> actors = new LinkedList<>();
        for (RootActor rootActor : rootActors) {
            actors.addAll(rootActor.getCardChildren());
        }
        while (!actors.isEmpty()) {
            ActorMachineCard actorMachineCard = actors.poll();
            actorMachineCard.updateCardPos();
            actorMachineCard.toFront();
            actorMachineCard.updateIngredientActors();
            for (MachineEdge edge : actorMachineCard.data.edges) {
                MachineNode node = edge.getChildNode();
                if (node != null) {
                    ActorMachineCard actorMachineNode = findActorMachineNode(node);
                    if (actorMachineNode != null) {
                        actors.add(actorMachineNode);
                    }
                }
            }
        }
    }

    public ActorMachineCard findActorMachineNode(MachineNode node) {
        Queue<ActorMachineCard> actorMachineCardsQueue = new LinkedList<>();
        for (RootActor rootActor : rootActors) {
            actorMachineCardsQueue.addAll(rootActor.getCardChildren());
            while (!actorMachineCardsQueue.isEmpty()) {
                ActorMachineCard actorMachineCard = actorMachineCardsQueue.poll();
                if (actorMachineCard.data == node) {
                    return actorMachineCard;
                }
                actorMachineCardsQueue.addAll(actorMachineCard.getCardChildren());
            }
        }
        return null;
    }
}
