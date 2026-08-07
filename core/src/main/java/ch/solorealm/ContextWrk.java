package ch.solorealm;

import ch.solorealm.actors.ActorMachineCard;
import ch.solorealm.actors.RootActor;
import ch.solorealm.actors.RootGridActor;
import ch.solorealm.beans.ContextUi;
import ch.solorealm.beans.RootGrid;
import ch.solorealm.beans.ingredient.IngredientCard;
import ch.solorealm.beans.levels.LevelGenerator;
import ch.solorealm.beans.levels.ShopNode;
import ch.solorealm.beans.levels.TableauNode;
import ch.solorealm.beans.machine.FoundationNode;
import ch.solorealm.beans.machine.MachineEdge;
import ch.solorealm.beans.machine.MachineNode;
import ch.solorealm.beans.machine.TrashMachine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ContextWrk implements ContextUi {
    private final Context context;
    private final DragAndDrop dndMachine;
    private final DragAndDrop dndIngredient;
    private RootGridActor tableau;
    private RootGridActor foundation;
    private RootGridActor shop1;
    private RootGridActor shop2;
    private final Set<RootGridActor> rootGridActors;
    private RootGridActor trash;

    public ContextWrk(Context context) {
        this.context = context;
        dndMachine = new DragAndDrop();
        dndMachine.setDragTime(0);
        dndIngredient = new DragAndDrop();
        dndIngredient.setDragTime(0);
        dndIngredient.setKeepWithinStage(false);
        rootGridActors = new HashSet<>();
    }

    public void createGrid(LevelGenerator levelGenerator) {
        context.stage.getActors().clear();

        tableau = new RootGridActor(new RootGrid(6), context.skin, context.assetManager.get("cards/empty_root.png"));
        rootGridActors.add(tableau);
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
                    updateAllGrid();
                    context.soundsManager.playCardDragDrop();
                }
            });
        }

        tableau.setPosition(480, 700);
        tableau.validate();
        context.stage.addActor(tableau);

        TextButton processButton = new TextButton("Process", context.skin);
        processButton.setPosition(1022, 50);
        processButton.setWidth(416);
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

        shop1 = new RootGridActor(new RootGrid(3), context.skin, context.assetManager.get("cards/empty_root.png"));
        rootGridActors.add(shop1);
        shop1.setPosition(1230, 450);
        shop1.validate();
        context.stage.addActor(shop1);


        shop2 = new RootGridActor(new RootGrid(3), context.skin, context.assetManager.get("cards/empty_root.png"));
        rootGridActors.add(shop2);
        shop2.setPosition(1230, 250);
        shop2.validate();
        context.stage.addActor(shop2);

        foundation = new RootGridActor(new RootGrid(3), context.skin, context.assetManager.get("cards/empty_root.png"));
        rootGridActors.add(foundation);
        foundation.setPosition(1230, 700);
        foundation.validate();
        context.stage.addActor(foundation);

        trash = new RootGridActor(new RootGrid(1), context.skin, context.assetManager.get("cards/empty_root.png"));
        rootGridActors.add(trash);
        trash.setPosition(108, 150);
        trash.validate();
        context.stage.addActor(trash);
        addActorTrashCard(new TrashMachine(), trash, 0);


        Image bgImage = new Image(context.assetManager.get("bg/bg.jpg", Texture.class));
        bgImage.setSize(1488, 837);
        context.stage.addActor(bgImage);
        bgImage.toBack();


        for (TableauNode initialTableau : levelGenerator.initialTableau) {
            addActorMachineCard(initialTableau.machineNode(), tableau.rootActors[initialTableau.index()]);
        }

        FoundationNode[] initialFoundation = levelGenerator.initialFoundation;
        for (int i = 0; i < initialFoundation.length; i++) {
            FoundationNode foundationNode = initialFoundation[i];
            RootGridActor shop = null;
            if (i == 0) {
                shop = shop1;
            } else if (i == 1) {
                shop = shop2;
            }

            Consumer<ActorMachineCard> onValidatedFondation;
            if (shop != null) {
                onValidatedFondation = unlockShop(shop);
            } else {
                onValidatedFondation = endLevel();
            }
            addActorFoundationCard(foundationNode, foundation, i, onValidatedFondation);
        }

        int shopI = 0;
        int widthI = 0;
        for (ShopNode actorCardData : levelGenerator.initialShop) {
            if (actorCardData.shopLevel() > shopI) {
                shopI = actorCardData.shopLevel();
                widthI = 0;
            }
            RootGridActor shop = null;
            if (shopI == 0) {
                shop = shop1;
            } else if (shopI == 1) {
                shop = shop2;
            }
            ActorMachineCard card = addActorShopCard(actorCardData.machineNodeSupplier(), shop.rootActors[widthI]);
            widthI += card.data.edges.length;
        }

        disableSimpleGrid(shop1);
        disableSimpleGrid(shop2);
        updateAllGrid();
    }

    private Consumer<ActorMachineCard> endLevel() {
        return (card) -> context.stage.addAction(Actions.sequence(
            Actions.run(context.soundsManager::playEndLevel),
            context.contextMenu.fadeTransition(true, false),
            Actions.parallel(
                Actions.run(context.contextMenu::createMenu),
                context.contextMenu.fadeTransition(false, true)
            )));
    }

    public void addActorMachineCard(MachineNode data, RootActor parent) {
        ActorMachineCard card = new ActorMachineCard(context.skin, data,
            context.assetManager.get(data.getAssetRecourcePath()),
            context.assetManager.get("cards/empty_card.png"),
            context.assetManager.get("machines/Grid_Overclocker_Upgrade.png"));
        card.setParentActor(parent);
        context.stage.addActor(card);

        addDndMachineSrc(card);
        addDndMachineDst(card);
        addDndIngredientDst(card);
    }

    public void addActorFoundationCard(MachineNode data, RootGridActor foundation, int index, Consumer<ActorMachineCard> onValidatedFondation) {
        RootActor parent = foundation.rootActors[index];
        ActorMachineCard card = new ActorMachineCard(context.skin, data,
            context.assetManager.get(data.getAssetRecourcePath()),
            context.assetManager.get("cards/empty_card.png"),
            context.assetManager.get("machines/Grid_Overclocker_Upgrade.png"));
        card.setParentActor(parent);
        context.stage.addActor(card);

        addDndIngredientDst(card, onValidatedFondation);
    }

    private void addActorTrashCard(TrashMachine data, RootGridActor trash, int index) {
        RootActor parent = trash.rootActors[index];
        ActorMachineCard card = new ActorMachineCard(context.skin, data,
            context.assetManager.get(data.getAssetRecourcePath()),
            context.assetManager.get("cards/empty_card.png"),
            context.assetManager.get("machines/Grid_Overclocker_Upgrade.png"));
        card.setParentActor(parent);
        context.stage.addActor(card);

        addDndIngredientDst(card, actorDrop -> {
            clearActorIngredientCard(card);
            card.data.edges[0].input = null;
        });

        addDndMachineDst(card, cardDrop -> {
            Stack<ActorMachineCard> stackMachine = new Stack<>();
            stackMachine.add(cardDrop);
            while (!stackMachine.isEmpty()) {
                ActorMachineCard machine = stackMachine.pop();
                stackMachine.addAll(machine.getCardChildren());
                clearActorIngredientCard(machine);
                machine.remove();
            }
            card.data.edges[0].setChildNode(null);
        });
    }

    private Consumer<ActorMachineCard> unlockShop(RootGridActor linkedShop) {
        return (newShopCard) -> {
            enableSimpleGrid(linkedShop);
            disableCard(newShopCard);
            context.soundsManager.playValidatedFoundation();
        };
    }

    public ActorMachineCard addActorShopCard(Supplier<MachineNode> machineNodeConstructor, RootActor parent) {
        MachineNode originalData = machineNodeConstructor.get();

        ActorMachineCard card = new ActorMachineCard(context.skin, originalData,
            context.assetManager.get(originalData.getAssetRecourcePath()),
            context.assetManager.get("cards/empty_card.png"),
            context.assetManager.get("machines/Grid_Overclocker_Upgrade.png"));
        card.setParentActor(parent);
        context.stage.addActor(card);

        addDndMachineSrc(card, Context.onlyEdgeTrigger(() -> shop1.findActorMachineNode(card.data) == null && shop2.findActorMachineNode(card.data) == null, () -> {
            addDndMachineDst(card);
            addDndIngredientDst(card);
            addActorShopCard(machineNodeConstructor, parent);
            updateAllGrid();
        }));
        return card;
    }

    private void addDndIngredientDst(ActorMachineCard card, Consumer<ActorMachineCard> drop) {
        card.dndIngredientDst = true;
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
                        moveActorIngredientCard(ingredientCard, ingredientCard.edgeAttached, machineEdge, inputSlot);
                        context.soundsManager.playIngredientDragDrop();
                        if (drop != null) {
                            drop.accept(card);
                        }
                    }
                });
            }
        }
    }

    private void addDndIngredientDst(ActorMachineCard card) {
        addDndIngredientDst(card, null);
    }

    private void addDndMachineDst(ActorMachineCard card) {
        addDndMachineDst(card, null);
    }

    private void addDndMachineDst(ActorMachineCard card, Consumer<ActorMachineCard> drop) {
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
                    updateAllGrid();
                    context.soundsManager.playCardDragDrop();
                    if (drop != null) {
                        drop.accept(dst);
                    }
                }
            });
        }
    }
    private void addDndMachineSrc(ActorMachineCard card) {
        addDndMachineSrc(card, null);
    }

    private void addDndMachineSrc(ActorMachineCard card, Runnable drop) {
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
                context.soundsManager.playCardDragStart();
                return payload;
            }
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                super.dragStop(event, x, y, pointer, payload, target);
                if (target == null) {
                    dndMachine.getDragActor().setPosition(originalPos.x, originalPos.y);
                    updateAllGrid();
                } else {
                    if (drop != null) {
                        drop.run();
                    }
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
    }

    @Override
    public void moveActorIngredientCard(IngredientCard ingredientCard, MachineEdge srcEdge, MachineEdge dstEdge, boolean dstInputSlot) {
        ActorMachineCard srcMachineCard = findActorMachineNodeAnyWhere(ingredientCard.edgeAttached.getNode());
        ActorMachineCard dstMachineCard = findActorMachineNodeAnyWhere(dstEdge.getNode());
        Actor ingredientActorCard = null;
        for (Actor actor : srcMachineCard.ingredientActorCards) {
            IngredientCard card = (IngredientCard) actor.getUserObject();
            if (card == ingredientCard) {
                ingredientActorCard = actor;
                break;
            }
        }

        Vector2 srcPos = new Vector2(ingredientActorCard.getX(), ingredientActorCard.getY());

        srcMachineCard.ingredientActorCards.remove(ingredientActorCard);
        ingredientActorCard.remove();

        srcEdge.moveIngredientCard(ingredientCard, dstEdge, dstInputSlot);

        dstMachineCard.ingredientActorCards.add(ingredientActorCard);
        dstMachineCard.addActorIngredientCard(dstEdge, ingredientActorCard, dstInputSlot);
        ingredientActorCard.moveBy(-ingredientActorCard.getWidth(), -ingredientActorCard.getHeight());

        Vector2 dstPos = new Vector2(ingredientActorCard.getX(), ingredientActorCard.getY());
        ingredientActorCard.setPosition(srcPos.x, srcPos.y);
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(dstPos.x, dstPos.y);
        moveToAction.setDuration(0.2f);
        ingredientActorCard.addAction(moveToAction);
    }

    @Override
    public void addActorIngredientCard(IngredientCard ingredientCard, MachineEdge edge, boolean inputSlot) {
        ActorMachineCard actorMachineNode = findActorMachineNodeAnyWhere(edge.getNode());
        if (actorMachineNode == null) return;
        Image ingredientActor = new Image(context.assetManager.get(ingredientCard.getAssetRecourcePath(), Texture.class));
        ingredientActor.setUserObject(ingredientCard);
        actorMachineNode.ingredientActorCards.add(ingredientActor);
        actorMachineNode.addActorIngredientCard(edge, ingredientActor, inputSlot);
        ingredientActor.setTouchable(Touchable.enabled);

        ingredientActor.setOrigin(ingredientActor.getWidth() / 2f, ingredientActor.getHeight() / 2f);
        ingredientActor.setScale(0);
        ScaleToAction scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1);
        scaleToAction.setDuration(0.2f);
        ingredientActor.addAction(scaleToAction);
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
                for (ActorMachineCard actorMachineCard : getAllActorMachineNodeAnyWhere()) {
                    if (actorMachineCard.dndIngredientDst) {
                        for (MachineEdge machineEdge : actorMachineCard.data.edges) {
                            if (machineEdge.isDropValide(ingredientCard, true)) {
                                Actor valideDropActor = actorMachineCard.edgeActorMap.get(machineEdge)[0];
                                valideDropActor.setColor(0.62f, 0.95f, 1f, 1f);
                                valideDropActors.add(valideDropActor);
                            }
                        }
                    }
                }
                context.soundsManager.playIngredientDragStart();
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
        clearActorIngredientCard(findActorMachineNodeAnyWhere(machineNode));
    }

    public void clearActorIngredientCard(ActorMachineCard actorMachineNode) {
        for (Actor ingredientActorCard : actorMachineNode.ingredientActorCards) {
            ingredientActorCard.remove();
        }
        actorMachineNode.ingredientActorCards.clear();
    }

    private void updateAllGrid() {
        tableau.updateActorDeep();
        foundation.updateActorSimple();
        shop1.updateActorSimple();
        shop2.updateActorSimple();
        trash.updateActorSimple();
    }

    private void process() {
        tableau.data.process(this);
        context.soundsManager.playProcess();
    }

    private Set<ActorMachineCard> getAllActorMachineNodeAnyWhere() {
        Set<ActorMachineCard> machines = new HashSet<>();
        Queue<ActorMachineCard> actorMachineCardsQueue = new LinkedList<>();
        for (RootActor rootActor : rootGridActors.stream()
                .flatMap(rootGridActor -> Arrays.stream(rootGridActor.rootActors)).toList()) {
            actorMachineCardsQueue.addAll(rootActor.getCardChildren());
            while (!actorMachineCardsQueue.isEmpty()) {
                ActorMachineCard actorMachineCard = actorMachineCardsQueue.poll();
                machines.add(actorMachineCard);
                actorMachineCardsQueue.addAll(actorMachineCard.getCardChildren());
            }
        }
        return machines;
    }

    private ActorMachineCard findActorMachineNodeAnyWhere(MachineNode node) {
        ActorMachineCard find = null;
        for (RootGridActor rootGridActor : rootGridActors) {
            find = rootGridActor.findActorMachineNode(node);
            if (find != null) {
                break;
            }
        }
        return find;
    }

    private void disableSimpleGrid(RootGridActor gridActor) {
        for (RootActor rootActor : gridActor.rootActors) {
            for (ActorMachineCard cardChild : rootActor.getCardChildren()) {
                disableCard(cardChild);
            }
        }
    }

    private void disableCard(ActorMachineCard card) {
        card.setTouchable(Touchable.disabled);
        card.setColor(Color.LIGHT_GRAY);
        for (Actor ingredientActorCard : card.ingredientActorCards) {
            ingredientActorCard.setTouchable(Touchable.disabled);
            ingredientActorCard.setColor(Color.LIGHT_GRAY);
        }
    }

    private void enableSimpleGrid(RootGridActor gridActor) {
        for (RootActor rootActor : gridActor.rootActors) {
            for (ActorMachineCard cardChild : rootActor.getCardChildren()) {
                cardChild.setTouchable(Touchable.enabled);
                cardChild.setColor(Color.WHITE);
            }
        }
    }

    public static String upperFirstLetter(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
