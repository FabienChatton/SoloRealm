package ch.solorealm;

import ch.solorealm.actors.ActorMachineCard;
import ch.solorealm.actors.RootActor;
import ch.solorealm.actors.RootGridActor;
import ch.solorealm.beans.ContextUi;
import ch.solorealm.beans.RootGrid;
import ch.solorealm.beans.ingredient.IngredientCard;
import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.machine.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public final class ContextWrk implements ContextUi {
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
        for (int i = 0; i < tableau.rootActors.length; i++) {
            RootActor rootActor = tableau.rootActors[i];
            int finalI = i;
            dnd.addTarget(new DragAndDrop.Target(rootActor) {
                @Override
                public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    ActorMachineCard machineCard = (ActorMachineCard) source.getActor();
                    if (!tableau.data.isDropValide(finalI, machineCard.data)) {
                        return false;
                    }
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

        TextButton processButton = new TextButton("Process", context.skin);
        processButton.setPosition(1000, 50);
        processButton.setWidth(440);
        processButton.setHeight(100);
        processButton.getLabel().setFontScale(3);
        processButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                process();
                return true;
            }
        });
        context.stage.addActor(processButton);

        // test
        createActorMachineCard(new AssemblingMachine(), tableau.rootActors[0]);
        createActorMachineCard(new FurnaceMachine(), tableau.rootActors[2]);
        createActorMachineCard(new AssemblingMachine(), tableau.rootActors[3]);
        createActorMachineCard(new MiningMachine(IngredientMaterial.COPPER), tableau.rootActors[5]);
    }

    public ActorMachineCard createActorMachineCard(MachineNode data) {
        return new ActorMachineCard(context.skin, data, context.assetManager.get(data.getAssetRecourcePath()), context.assetManager.get("cards/empty_card.png"), context.assetManager.get("machines/Grid_Overclocker_Upgrade.png"));
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
                    for (ActorMachineCard cardChild : card.getCardChildren()) {
                        cardChild.setParentActorR();
                    }
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
                    ActorMachineCard dst = (ActorMachineCard) source.getActor();
                    if (!tableau.data.isDropValide(card.data, finalI, dst.data)) {
                        return false;
                    }
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

    @Override
    public void addActorIngredientCard(IngredientCard ingredientCard, MachineEdge edge, boolean inputSlot) {
        ActorMachineCard actorMachineNode = findActorMachineNode(edge.getNode());
        Actor edgeActor = actorMachineNode.getEdgeActor(edge, inputSlot);
        Vector2 stageCoordinates = edgeActor.localToStageCoordinates(new Vector2(edgeActor.getX(), edgeActor.getY()));
        Image ingredientImage = new Image(context.assetManager.get(ingredientCard.getAssetRecourcePath(), Texture.class));
        ingredientImage.setPosition(stageCoordinates.x - 58, stageCoordinates.y);
        actorMachineNode.addActor(ingredientImage);
        context.stage.addActor(ingredientImage);
    }

    private void process() {
        tableau.data.process(this);
    }

    private Set<ActorMachineCard> getAllActorMachineNode() {
        Set<ActorMachineCard> machines = new HashSet<>();
        Queue<ActorMachineCard> actorMachineCardsQueue = new LinkedList<>();
        for (RootActor rootActor : tableau.rootActors) {
            actorMachineCardsQueue.addAll(rootActor.getCardChildren());
            while (!actorMachineCardsQueue.isEmpty()) {
                ActorMachineCard actorMachineCard = actorMachineCardsQueue.poll();
                machines.add(actorMachineCard);
                actorMachineCardsQueue.addAll(actorMachineCard.getCardChildren());
            }
        }
        return machines;
    }

    private ActorMachineCard findActorMachineNode(MachineNode node) {
        Queue<ActorMachineCard> actorMachineCardsQueue = new LinkedList<>();
        for (RootActor rootActor : tableau.rootActors) {
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
