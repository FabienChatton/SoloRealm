package ch.solorealm;

import ch.solorealm.actors.ActorMachineCard;
import ch.solorealm.actors.RootActor;
import ch.solorealm.actors.RootGridActor;
import ch.solorealm.actors.ShowHelp;
import ch.solorealm.beans.ContextUi;
import ch.solorealm.beans.RootGrid;
import ch.solorealm.beans.ingredient.IngredientCard;
import ch.solorealm.beans.ingredient.IngredientMaterial;
import ch.solorealm.beans.ingredient.IngredientPair;
import ch.solorealm.beans.levels.LevelGenerator;
import ch.solorealm.beans.levels.LevelStat;
import ch.solorealm.beans.levels.ShopNode;
import ch.solorealm.beans.machine.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ContextWrk implements ContextUi {
    public final Context context;
    private final DragAndDrop dndMachine;
    private final DragAndDrop dndIngredient;
    private RootGridActor tableau;
    private RootGridActor foundation;
    private RootGridActor shop1;
    private RootGridActor shop2;
    private final Set<RootGridActor> rootGridActors;
    private RootGridActor trash;
    private Table helpWindow;
    private LevelStat levelStat;
    private ContextLevelEnd contextLevelEnd;
    private boolean recordScreenshot;
    private Class<? extends LevelGenerator> levelGeneratorClass;

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
        InputMultiplexer inputMultiplexer = getInputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        levelGeneratorClass = levelGenerator.getClass();

        tableau = new RootGridActor(new RootGrid(6), context.skin, context.assetManager.get("cards/empty_root.png"));
        rootGridActors.add(tableau);
        for (int i = 0; i < tableau.rootActors.length; i++) {
            RootActor rootActor = tableau.rootActors[i];
            int finalI = i;
            dndMachine.addTarget(new DragAndDrop.Target(rootActor) {
                @Override
                public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    ActorMachineCard machineCard = (ActorMachineCard) source.getActor();
                    if (!tableau.data.isDropValide(rootActor.data, 0, machineCard.data)) {
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
                    levelStat.nbrMovePlus1();
                    takeScreenShot();
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

        contextLevelEnd = new ContextLevelEnd(context, levelGenerator.getClass().getSimpleName());
        Table statTable = contextLevelEnd.createStatTable();

        context.stage.addActor(statTable);
        levelStat = contextLevelEnd.getLevelStat();

        setLevel(levelGenerator);

        recordScreenshot = true;

        disableSimpleGrid(shop1);
        disableSimpleGrid(shop2);
        updateAllGrid();
    }

    private void setLevel(LevelGenerator levelGenerator) {
        for (int i = 0; i < levelGenerator.initialTableau.length;) {
            MachineNode machine = levelGenerator.initialTableau[i];
            addActorMachineCard(machine, tableau.rootActors[i]);
            i += machine.edges.length;
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
    }

    private InputMultiplexer getInputMultiplexer() {
        InputAdapter inputAdapter = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    levelStat.dispose();
                    context.stage.addAction(Actions.sequence(context.contextMenu.fadeToMenu()));
                    Gdx.input.setInputProcessor(context.stage);
                    return true;
                } else if (keycode == Input.Keys.F1) {
                    reloadLevel();
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (button == 1) {
                    if (helpWindow != null) {
                        helpWindow.remove();
                    }
                    return true;
                }
                return false;
            }
        };
        return new InputMultiplexer(inputAdapter, context.stage);
    }

    public void showHelpWindow(IngredientPair pair) {
        Table table = new Table();
        Label label = new Label(FoundationNode.getMachineDisplayName(pair.material(), pair.type()), context.skin);
        label.setColor(Color.BLACK);
        table.add(label);

        Image image = new Image(getTextureOrAny(pair));
        table.add(image).size(32).padLeft(8).row();
        Map<Class<MachineNode>, Collection<MachineProcessRecipe>> matchRecipe = context.recipeWrk.getMatchRecipe(pair);
        for (Class<MachineNode> machineNodeClass : matchRecipe.keySet()) {
            MachineNode machineNode;
            try {
                machineNode = machineNodeClass.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                Gdx.app.log("Recipe", "Can not load machine node new instance.");
                continue;
            }
            table.add(ContextMenu.getDivider()).fillX().height(1).colspan(2).padBottom(20).row();
            Label machineName = new Label(machineNode.getMachineDisplayName(), context.skin);
            machineName.setColor(Color.BLACK);
            table.add(machineName);
            Image machineImage = new Image(context.assetManager.get(machineNode.getAssetResourcePath(), Texture.class));
            table.add(machineImage).size(32).row();
            for (MachineProcessRecipe machineProcessRecipe : matchRecipe.get(machineNodeClass)) {
                addMachineProcessHelp(machineProcessRecipe, table);
            }
            table.add(ContextMenu.getDivider()).fillX().height(1).colspan(2).row();
        }
        addHelpWindow(table);
    }

    public void showHelpWindow(ActorMachineCard card) {
        Table helpContent = new Table();
        boolean addDivider = card.data.machineProcessRecipes.size() > 1;
        for (MachineProcessRecipe machineProcessRecipe : card.data.machineProcessRecipes) {
            if (addDivider) {
                helpContent.add(ContextMenu.getDivider()).fillX().height(1).colspan(2).row();
            }
            addMachineProcessHelp(machineProcessRecipe, helpContent);
            if (addDivider) {
                helpContent.add(ContextMenu.getDivider()).fillX().height(1).colspan(2).row();
            }
        }
        addHelpWindow(helpContent);
    }

    private void addMachineProcessHelp(MachineProcessRecipe machineProcessRecipe, Table helpContent) {
        Table row = new Table();
        int i = 0;
        Table inputTable = new Table();
        for (IngredientPair ingredientPair : machineProcessRecipe.input()) {
            Label label = new Label(FoundationNode.getMachineDisplayName(ingredientPair.material(), ingredientPair.type()), context.skin);
            label.setColor(Color.BLACK);
            inputTable.add(label);
            String assetResourcePath = IngredientCard.getAssetResourcePath(ingredientPair.material(), ingredientPair.type());
            Image img = new Image(context.assetManager.get(assetResourcePath, Texture.class));
            inputTable.add(img).size(32).padLeft(8);
            if (++i != machineProcessRecipe.input().length) {
                inputTable.row();
            }
        }
        row.add(inputTable).expandX().left();
        if (machineProcessRecipe.output() != null) {
            Label arrowLabel = new Label("->", context.skin);
            arrowLabel.setColor(Color.BLACK);
            row.add(arrowLabel).padLeft(8);
            Label label = new Label(FoundationNode.getMachineDisplayName(machineProcessRecipe.output().material(), machineProcessRecipe.output().type()), context.skin);
            label.setColor(Color.BLACK);
            row.add(label).right().expandX().padLeft(8);
            Image img = new Image(getTextureOrAny(machineProcessRecipe.output()));
            row.add(img).right().size(32).padLeft(8);
        }
        helpContent.add(row).expand().fill().colspan(2).row();
    }

    private void addHelpWindow(Actor helpContent) {
        int screenX = Gdx.input.getX();
        int screenY = Gdx.input.getY();
        Vector2 stageCoords = new Vector2(screenX, screenY);
        context.stage.screenToStageCoordinates(stageCoords);
        helpWindow = new Table(context.skin);
        helpWindow.add(helpContent);
        helpWindow.setPosition(stageCoords.x, stageCoords.y);

        Pixmap pixmap = new Pixmap(3, 3, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        pixmap.setColor(new Color(0.8627f, 0.8039f, 0.6745f, 1f));
        pixmap.fillRectangle(1, 1, 1, 1);
        Texture texture = new Texture(pixmap);

        NinePatchDrawable backgroundDrawable = new NinePatchDrawable(new NinePatch(texture, 1, 1, 1, 1));

        helpWindow.setBackground(backgroundDrawable);

        helpWindow.pad(10f);
        helpWindow.setSize(1, 1);

        helpWindow.pack();
        if (helpWindow.getHeight() + stageCoords.y > context.stage.getHeight()) {
            helpWindow.setY(stageCoords.y - (helpWindow.getHeight() + stageCoords.y - context.stage.getHeight()));
        }
        if (helpWindow.getWidth() + stageCoords.x > context.stage.getWidth()) {
            helpWindow.setX(stageCoords.x - (helpWindow.getWidth() + stageCoords.x - context.stage.getWidth()));
        }
        context.stage.addActor(helpWindow);
    }

    public InputListener getHelpWindowListener(ShowHelp card) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == 1) {
                    card.showHelp();
                }
                return true;
            }
        };
    }

    private void reloadLevel() {
        try {
            LevelGenerator levelGenerator = levelGeneratorClass.getConstructor().newInstance();
            createGrid(levelGenerator);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void setBlackFadeBackground(Table table) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 0,0, 0.85f));
        pixmap.fill();

        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();
        table.setBackground(backgroundDrawable);
    }

    private Consumer<ActorMachineCard> endLevel() {
        return (card) -> context.stage.addAction(Actions.sequence(
            Actions.run(context.soundsManager::playEndLevel),
            context.contextMenu.fadeTransition(true, false),
            Actions.parallel(
                Actions.run(() -> contextLevelEnd.setLevelEnd()),
                context.contextMenu.fadeTransition(false, true)
            )
        ));
    }

    public void addActorMachineCard(MachineNode data, RootActor parent) {
        ActorMachineCard card = new ActorMachineCard(this, data,
            context.assetManager.get(data.getAssetResourcePath()),
            context.assetManager.get("cards/empty_card.png"),
            context.assetManager.get("machines/Grid_Overclocker_Upgrade.png"));
        card.setParentActor(parent);
        context.stage.addActor(card);

        addDndMachineSrc(card);
        addDndMachineDst(card);
        addDndIngredientDst(card);

        levelStat.nbrCardPlus1();
        takeScreenShot();
    }

    public void addActorFoundationCard(FoundationNode data, RootGridActor foundation, int index, Consumer<ActorMachineCard> onValidatedFondation) {
        RootActor parent = foundation.rootActors[index];
        ActorMachineCard card = new ActorMachineCard(this, data,
            context.assetManager.get(data.getAssetResourcePath()),
            context.assetManager.get("cards/empty_card.png"),
            context.assetManager.get("machines/Grid_Overclocker_Upgrade.png"));
        card.setShowHelp(() -> showHelpWindow(new IngredientPair(data.ingredientMaterial, data.ingredientType)));
        card.setParentActor(parent);
        context.stage.addActor(card);

        addDndIngredientDst(card, onValidatedFondation);
    }

    private void addActorTrashCard(TrashMachine data, RootGridActor trash, int index) {
        RootActor parent = trash.rootActors[index];
        ActorMachineCard card = new ActorMachineCard(this, data,
            context.assetManager.get(data.getAssetResourcePath()),
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

        ActorMachineCard card = new ActorMachineCard(this, originalData,
            context.assetManager.get(originalData.getAssetResourcePath()),
            context.assetManager.get("cards/empty_card.png"),
            context.assetManager.get("machines/Grid_Overclocker_Upgrade.png"));
        card.setParentActor(parent);
        context.stage.addActor(card);

        addDndMachineSrc(card, Context.onlyEdgeTrigger(() -> shop1.findActorMachineNode(card.data) == null && shop2.findActorMachineNode(card.data) == null, () -> {
            addDndMachineDst(card);
            addDndIngredientDst(card);
            addActorShopCard(machineNodeConstructor, parent);
            updateAllGrid();
            levelStat.nbrCardPlus1();
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
                        levelStat.nbrMovePlus1();
                        takeScreenShot();
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
                    levelStat.nbrMovePlus1();
                    takeScreenShot();
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
        Image ingredientActor = new Image(getTextureOrAny(ingredientCard));
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
        ingredientActor.addListener(getHelpWindowListener(() -> showHelpWindow(IngredientPair.from(ingredientCard))));
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
        levelStat.nbrProcessPlus1();
        takeScreenShot();
    }

    private void takeScreenShot() {
        if (recordScreenshot) {
            Pixmap pixmap = Pixmap.createFromFrameBuffer(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            levelStat.addProcessScreenShot(pixmap);
        }
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

    private Texture getTextureOrAny(IngredientCard data) {
        Texture texture;
        try {
            texture = context.assetManager.get(data.getAssetResourcePath(), Texture.class);
        } catch (GdxRuntimeException e) {
            texture = context.assetManager.get(IngredientCard.getAssetResourcePath(IngredientMaterial.ANY, data.ingredientType));
        }
        return texture;
    }

    private Texture getTextureOrAny(IngredientPair data) {
        Texture texture;
        String assetResourcePath = IngredientCard.getAssetResourcePath(data.material(), data.type());
        try {
            texture = context.assetManager.get(assetResourcePath, Texture.class);
        } catch (GdxRuntimeException e) {
            Gdx.app.debug("Mising Texture", "The texture \"" + assetResourcePath + "\" is missing.");
            texture = context.assetManager.get(IngredientCard.getAssetResourcePath(IngredientMaterial.ANY, data.type()));
        }
        return texture;
    }

    public static String upperFirstLetter(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
