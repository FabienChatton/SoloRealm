package ch.solorealm.actors;

import ch.solorealm.beans.ingredient.IngredientCard;
import ch.solorealm.beans.machine.MachineEdge;
import ch.solorealm.beans.machine.MachineNode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.*;

public class ActorMachineCard extends Table implements GetCardChildren {
    public final MachineNode data;
    public final Actor[] edgeDropActor;
    public final Map<MachineEdge, Actor[]> edgeActorMap;
    public final Collection<Actor> ingredientActorCards;
    private final Image icon;
    private final List<ActorMachineCard> cardActorChild;
    private Table edgeTable;
    private GetCardChildren cardActorParent;
    private Actor edgeParentActor;
    private static final float SCALE_FACTOR = 1.5f;

    public ActorMachineCard(Skin skin, MachineNode data, Texture iconTexture, Texture backgroundTexture, Texture arrowTexture) {
        this.data = data;
        this.icon = new Image(iconTexture);
        this.cardActorChild = new ArrayList<>();
        this.ingredientActorCards = new HashSet<>();
        this.edgeActorMap = new HashMap<>();

        float edgeWidth = backgroundTexture.getWidth() * SCALE_FACTOR;
        float edgeHeight = backgroundTexture.getHeight() * SCALE_FACTOR;
        float totalWidth = edgeWidth * data.edges.length + (32 * (data.edges.length - 1));
        this.setSize(totalWidth, edgeHeight);

        NinePatchDrawable background = new NinePatchDrawable(new NinePatch(backgroundTexture));
        this.background(background);

        Table edgesRowTable = new Table();

        for (int i = 0; i < data.edges.length; i++) {
            MachineEdge edge = data.edges[i];
            edgeTable = new Table();

            float arrowW = arrowTexture.getWidth() * SCALE_FACTOR;
            float arrowH = arrowTexture.getHeight() * SCALE_FACTOR;

            Image inputArrowImage = null;
            Image outputArrowImage = null;
            if (edge.inputType != null) {
                inputArrowImage = new Image(arrowTexture);
                inputArrowImage.setOrigin(arrowW / 2f, arrowH / 2f);
                inputArrowImage.rotateBy(180);
                inputArrowImage.setAlign(Align.center);
                edgeTable.add(inputArrowImage).size(arrowW, arrowH);
            } else {
                edgeTable.add().size(arrowW, arrowH);
            }

            if (edge.outputType != null) {
                outputArrowImage = new Image(arrowTexture);
                edgeTable.add(outputArrowImage).size(arrowW, arrowH);
            } else {
                edgeTable.add().size(arrowW, arrowH);
            }

            edgeActorMap.put(edge, new Actor[]{inputArrowImage, outputArrowImage});

            edgesRowTable.add(edgeTable).width(edgeWidth).center();

            if (i < data.edges.length - 1) {
                edgesRowTable.add().width(32);
            }
        }

        Table contentTable = new Table();
        contentTable.add(edgesRowTable).expandX().fillX().row();

        Label machineNameLabel = new Label(data.getMachineDisplayName(), skin, "window");
        machineNameLabel.setColor(Color.BLACK);
        machineNameLabel.setFontScale(SCALE_FACTOR);

        contentTable.add(machineNameLabel).center().pad(5 * SCALE_FACTOR).row();
        contentTable.add(icon).size(64 * SCALE_FACTOR, 64 * SCALE_FACTOR).expandY().row();
        this.add(contentTable).width(totalWidth).height(edgeHeight);

        setTouchable(Touchable.enabled);

        edgeDropActor = new Actor[data.edges.length];
        for (int i = 0; i < data.edges.length; i++) {
            Actor dropEdgeActor = new Actor();
            dropEdgeActor.setSize(edgeWidth, 120);
            dropEdgeActor.setPosition((edgeWidth * i) + 32 * i, 0);
            addActor(dropEdgeActor);
            edgeDropActor[i] = dropEdgeActor;
        }
    }

    public void setParentActor(RootActor rootActor) {
        if (cardActorParent != null) {
            cardActorParent.getCardChildren().remove(this);
            data.getParent().setChildNode(null);
        }
        cardActorParent = rootActor;
        cardActorParent.getCardChildren().add(this);
        edgeParentActor = rootActor;

        data.setParent(rootActor.data.edges[0]);
        data.getParent().setChildNode(data);
        rootActor.validate();
    }

    public void setParentActor(ActorMachineCard newParentCard, MachineEdge edge, Actor edgeParentActor) {
        cardActorParent.getCardChildren().remove(this);
        data.getParent().setChildNode(null);
        cardActorParent = newParentCard;
        cardActorParent.getCardChildren().add(this);

        data.setParent(edge);
        data.getParent().setChildNode(data);
        validate();
        this.edgeParentActor = edgeParentActor;
    }

    public void updateCardPos() {
        int padY = 0;
        if (cardActorParent != edgeParentActor) {
            padY = -80;
        }
        Vector2 stageCoordinate = edgeParentActor.localToStageCoordinates(new Vector2(0, padY));
        setPosition(stageCoordinate.x, stageCoordinate.y);
    }

    @Override
    public List<ActorMachineCard> getCardChildren() {
        return cardActorChild;
    }

    public void moveByR(float x, float y) {
        toFront();
        for (ActorMachineCard cardChild : getCardChildren()) {
            cardChild.moveByR(x, y);
        }
        moveBy(x, y);
        updateIngredientActors();
    }

    public void addActorIngredientCard(MachineEdge edge, Actor ingredientActor, boolean inputSlot) {
        Actor edgeActor = getEdgeActor(edge, inputSlot);
        if (edgeActor == null) return;
        Vector2 edgeActorCoords = edgeActor.localToStageCoordinates(new Vector2(0, 0));
        ingredientActor.setPosition(edgeActorCoords.x, edgeActorCoords.y);
        if (ingredientActor.getWidth() < 48) {
            float scale = 48f / ingredientActor.getWidth();
            ingredientActor.setSize(ingredientActor.getWidth() * scale, ingredientActor.getWidth() * scale);
        }
        getStage().addActor(ingredientActor);
    }

    public void updateIngredientActors() {
        for (Actor ingredientActor : ingredientActorCards) {
            IngredientCard ingredientCard = (IngredientCard) ingredientActor.getUserObject();
            boolean inputSlot = ingredientCard.edgeAttached.input == ingredientCard;
            Actor edgeActor = getEdgeActor(ingredientCard.edgeAttached, inputSlot);
            Vector2 edgeActorCoords = edgeActor.localToStageCoordinates(new Vector2(0, 0));
            ingredientActor.setPosition(edgeActorCoords.x, edgeActorCoords.y);
            if (inputSlot) {
                ingredientActor.moveBy(-ingredientActor.getWidth(), -ingredientActor.getHeight());
            }
            ingredientActor.toFront();
        }
    }

    private Actor getEdgeActor(MachineEdge edge, boolean inputSlot) {
        Actor[] actorActorEntry = edgeActorMap.get(edge);
        if (actorActorEntry == null) return null;
        if (inputSlot) {
            return actorActorEntry[0];
        } else {
            return actorActorEntry[1];
        }
    }
}
