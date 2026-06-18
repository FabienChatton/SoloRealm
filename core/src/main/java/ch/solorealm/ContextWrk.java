package ch.solorealm;

import ch.solorealm.actors.ActorMachineCard;
import ch.solorealm.actors.RootActor;
import ch.solorealm.actors.RootGridActor;
import ch.solorealm.beans.RootGrid;
import ch.solorealm.beans.machine.AssemblingMachine;
import ch.solorealm.beans.machine.FurnaceMachine;
import ch.solorealm.beans.machine.MachineNode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;

public final class ContextWrk {
    private final Context context;
    private final DragAndDrop dnd;
    private RootGridActor tableau;

    public ContextWrk(Context context) {
        this.context = context;
        dnd = new DragAndDrop();
        dnd.setDragTime(0);
    }

    public void createGrid() {
        tableau = new RootGridActor(new RootGrid(6), context.skin, context.assetManager.get("cards/empty_root.png"));
        for (RootActor rootActor : tableau.rootActors) {
            dnd.addTarget(new DragAndDrop.Target(rootActor) {
                @Override
                public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    rootActor.backgroundImage.setColor(0.62f,0.95f,1f, 1f);
                    return true;
                }

                @Override
                public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
                    rootActor.backgroundImage.setColor(Color.WHITE);
                }

                @Override
                public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    ActorMachineCard machineCard = (ActorMachineCard) source.getActor();
                    machineCard.setParentActor(rootActor);
                }
            });
        }

        tableau.setPosition(500, 700);
        tableau.validate();
        context.stage.addActor(tableau);

        // test
        createActorMachineCard(new AssemblingMachine(), tableau.rootActors[0]);
        createActorMachineCard(new FurnaceMachine(), tableau.rootActors[2]);
    }

    public ActorMachineCard createActorMachineCard(MachineNode data) {
        return new ActorMachineCard(context.skin, data, context.assetManager.get(data.getAssetName()), context.assetManager.get("cards/empty_card.png"), context.assetManager.get("machines/Grid_Overclocker_Upgrade.png"));
    }

    public void createActorMachineCard(MachineNode data, RootActor parent) {
        ActorMachineCard card = createActorMachineCard(data);
        card.setParentActor(parent);
        context.stage.addActor(card);

        dnd.addSource(new DragAndDrop.Source(card) {
            private final Vector2 originalPos = new Vector2();
            private final Vector2 deltaPos = new Vector2();
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setDragActor(card);
                dnd.setDragActorPosition(card.getWidth() - x, -y);
                originalPos.set(card.getX(), card.getY());
                deltaPos.set(card.getX(), card.getY());
                card.toFront();
                return payload;
            }
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                super.dragStop(event, x, y, pointer, payload, target);
                if (target == null) {
                    dnd.getDragActor().setPosition(originalPos.x, originalPos.y);
                }
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                deltaPos.set(card.getX() - deltaPos.x, card.getY() - deltaPos.y);
                for (ActorMachineCard cardChild : card.getCardChildren()) {
                    cardChild.moveByR(deltaPos.x, deltaPos.y);
                }
                deltaPos.set(card.getX(), card.getY());
            }
        });

        for (int i = 0; i < card.edgeDropActor.length; i++) {
            Actor dropActor = card.edgeDropActor[i];
            int finalI = i;
            dnd.addTarget(new DragAndDrop.Target(dropActor) {
                @Override
                public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    card.setColor(0.62f,0.95f,1f, 1f);
                    return true;
                }

                @Override
                public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
                    card.setColor(Color.WHITE);
                }

                @Override
                public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    ActorMachineCard dst = (ActorMachineCard) source.getActor();
                    dst.setParentActor(card, card.data.edges[finalI], dropActor);
                }
            });
        }
    }
}
