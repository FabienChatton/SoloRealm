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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.*;

public final class ContextWrk implements ContextUi {
    private final Context context;
    private final DragAndDrop dndMachine;
    private final DragAndDrop dndIngredient;
    private RootGridActor tableau;

    public ContextWrk(Context context) {
        this.context = context;
        dndMachine = new DragAndDrop();
        dndMachine.setDragTime(0);
        dndIngredient = new DragAndDrop();
        dndIngredient.setDragTime(0);
        dndIngredient.setKeepWithinStage(false);
    }

    public void createGrid() {
        tableau = new RootGridActor(new RootGrid(6), context.skin, context.assetManager.get("cards/empty_root.png"));
        for (int i = 0; i < tableau.rootActors.length; i++) {
            RootActor rootActor = tableau.rootActors[i];
            int finalI = i;
            dndMachine.addTarget(new DragAndDrop.Target(rootActor) {
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

        dndMachine.addSource(new DragAndDrop.Source(card) {
            private final Vector2 originalPos = new Vector2();
            private final Vector2 deltaPos = new Vector2();
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                // test if the click is on the ingredient texture or not
                // so if it is on the ingredient texture, it can do the ingredient drag and drop
                Actor hit = card.hit(x, y, true);
                if (hit instanceof Image image && image.getDrawable() instanceof TextureRegionDrawable region) {
                    if (!region.getRegion().getTexture().toString().contains("Grid_Overclocker_Upgrade.png")) {
                        return null;
                    }
                }
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setDragActor(card);
                dndMachine.setDragActorPosition(card.getWidth() - x, -y);
                originalPos.set(card.getX(), card.getY());
                deltaPos.set(card.getX(), card.getY());
                card.toFront();
                return payload;
            }
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                super.dragStop(event, x, y, pointer, payload, target);
                if (target == null) {
                    dndMachine.getDragActor().setPosition(originalPos.x, originalPos.y);
                    for (ActorMachineCard cardChild : card.getCardChildren()) {
                        cardChild.setParentActorR();
                    }
                    card.updateIngredientActors();
                }
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                deltaPos.set(card.getX() - deltaPos.x, card.getY() - deltaPos.y);
                for (ActorMachineCard cardChild : card.getCardChildren()) {
                    cardChild.moveByR(deltaPos.x, deltaPos.y);
                }
                for (Actor ingredientActorCard : card.ingredientActorCards) {
                    ingredientActorCard.moveBy(deltaPos.x, deltaPos.y);
                    ingredientActorCard.toFront();
                }
                deltaPos.set(card.getX(), card.getY());
            }
        });

        for (int i = 0; i < card.edgeDropActor.length; i++) {
            Actor dropActor = card.edgeDropActor[i];
            int finalI = i;
            dndMachine.addTarget(new DragAndDrop.Target(dropActor) {
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
        for (MachineEdge machineEdge : card.edgeActorMap.keySet()) {
            for (int i = 0; i < card.edgeActorMap.get(machineEdge).length; i++) {
                Actor edgeArrowImage = card.edgeActorMap.get(machineEdge)[i];
                if (edgeArrowImage == null) continue;
                final boolean inputSlot = i % 2 == 0;
                dndIngredient.addTarget(new DragAndDrop.Target(edgeArrowImage) {
                    @Override
                    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                        IngredientCard ingredientCard = (IngredientCard) payload.getObject();
                        return machineEdge.isDropValide(ingredientCard, inputSlot);
                    }

                    @Override
                    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                        IngredientCard ingredientCard = (IngredientCard) payload.getObject();

                        ActorMachineCard srcMachineCard = findActorMachineNode(ingredientCard.edgeAttached.getNode());
                        ActorMachineCard dstMachineCard = findActorMachineNode(machineEdge.getNode());
                        Actor dragActor = payload.getDragActor();
                        srcMachineCard.ingredientActorCards.remove(dragActor);
                        dragActor.remove();

                        ingredientCard.edgeAttached.moveIngredientCard(ingredientCard, machineEdge, inputSlot);

                        dstMachineCard.ingredientActorCards.add(dragActor);
                        dstMachineCard.addActorIngredientCard(machineEdge, dragActor, inputSlot);
                        dragActor.moveBy(-dragActor.getWidth(), -dragActor.getHeight());
                    }
                });
            }
        }
    }

    @Override
    public void addActorIngredientCard(IngredientCard ingredientCard, MachineEdge edge, boolean inputSlot) {
        ActorMachineCard actorMachineNode = findActorMachineNode(edge.getNode());
        if (actorMachineNode == null) return;
        Image ingredientActor = new Image(context.assetManager.get(ingredientCard.getAssetRecourcePath(), Texture.class));
        ingredientActor.setUserObject(ingredientCard);
        actorMachineNode.ingredientActorCards.add(ingredientActor);
        actorMachineNode.addActorIngredientCard(edge, ingredientActor, inputSlot);
        ingredientActor.setTouchable(Touchable.enabled);
        dndIngredient.addSource(new DragAndDrop.Source(ingredientActor) {
            private final Vector2 originalPos = new Vector2();
            private final Vector2 deltaPos = new Vector2();
            private final Collection<Actor> valideDropActors = new HashSet<>();
            @Override
            public DragAndDrop.Payload dragStart(InputEvent inputEvent, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setDragActor(ingredientActor);
                payload.setObject(ingredientCard);
                dndIngredient.setDragActorPosition(-ingredientActor.getParent().getX() + ingredientActor.getWidth() / 2, -ingredientActor.getParent().getY() - ingredientActor.getHeight() / 2);
                originalPos.set(ingredientActor.getX(), ingredientActor.getY());
                deltaPos.set(ingredientActor.getX(), ingredientActor.getY());
                ingredientActor.toFront();

                //valide drop color
                for (ActorMachineCard actorMachineCard : getAllActorMachineNode()) {
                    for (MachineEdge machineEdge : actorMachineCard.data.edges) {
                        if (machineEdge.isDropValide(ingredientCard, true)) {
                            Actor valideDropActor = actorMachineCard.edgeActorMap.get(machineEdge)[0];
                            valideDropActor.setColor(0.62f,0.95f,1f, 1f);
                            valideDropActors.add(valideDropActor);
                        }
                    }
                }
                return payload;
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                super.dragStop(event, x, y, pointer, payload, target);
                if (target == null) {
                    dndIngredient.getDragActor().setPosition(originalPos.x, originalPos.y);
                }
                for (Actor valideDropActor : valideDropActors) {
                    valideDropActor.setColor(Color.WHITE);
                }
            }
        });
    }

    @Override
    public void clearActorIngredientCard(MachineNode machineNode) {
        ActorMachineCard actorMachineNode = findActorMachineNode(machineNode);
        for (Actor ingredientActorCard : actorMachineNode.ingredientActorCards) {
            ingredientActorCard.remove();
        }
        actorMachineNode.ingredientActorCards.clear();
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
