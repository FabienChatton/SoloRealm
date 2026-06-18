package ch.solorealm.actors;

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

import java.util.ArrayList;
import java.util.List;

public class ActorMachineCard extends Table implements GetCardChildren {
    public final MachineNode data;
    private final Image icon;
    public final Actor[] edgeDropActor;
    private final List<ActorMachineCard> cardActorChild;
    private GetCardChildren cardActorParent;
    private static final float SCALE_FACTOR = 1.5f;

    public ActorMachineCard(Skin skin, MachineNode data, Texture iconTexture, Texture backgroundTexture, Texture arrowTexture) {
        this.data = data;
        this.icon = new Image(iconTexture);
        this.cardActorChild = new ArrayList<>();

        float edgeWidth = backgroundTexture.getWidth() * SCALE_FACTOR;
        float edgeHeight = backgroundTexture.getHeight() * SCALE_FACTOR;
        float totalWidth = edgeWidth * data.edges.length + (32 * (data.edges.length - 1));
        this.setSize(totalWidth, edgeHeight);

        NinePatchDrawable background = new NinePatchDrawable(new NinePatch(backgroundTexture));
        this.background(background);

        Table edgesRowTable = new Table();

        for (int i = 0; i < data.edges.length; i++) {
            MachineEdge edge = data.edges[i];
            Table edgeTable = new Table();

            float arrowW = arrowTexture.getWidth() * SCALE_FACTOR;
            float arrowH = arrowTexture.getHeight() * SCALE_FACTOR;

            if (edge.outputType != null) {
                Image arrowImage = new Image(arrowTexture);
                arrowImage.setOrigin(arrowW / 2f, arrowH / 2f);
                arrowImage.rotateBy(180);
                arrowImage.setAlign(Align.center);
                edgeTable.add(arrowImage).size(arrowW, arrowH);
            } else {
                edgeTable.add().size(arrowW, arrowH);
            }

            if (edge.inputType != null) {
                edgeTable.add(new Image(arrowTexture)).size(arrowW, arrowH);
            } else {
                edgeTable.add().size(arrowW, arrowH);
            }

            edgesRowTable.add(edgeTable).width(edgeWidth).center();

            if (i < data.edges.length - 1) {
                edgesRowTable.add().width(32);
            }
        }

        Table contentTable = new Table();
        contentTable.add(edgesRowTable).expandX().fillX().row();

        Label machineNameLabel = new Label(data.getMachineName(), skin, "window");
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
        }
        cardActorParent = rootActor;
        cardActorParent.getCardChildren().add(this);

        data.setParent(rootActor.data.edges[0]);
        rootActor.validate();
        Vector2 stageCoordinate = rootActor.localToStageCoordinates(new Vector2(0, 0));
        setPosition(stageCoordinate.x, stageCoordinate.y);
        toFront();
    }

    public void setParentActor(ActorMachineCard newParentCard, MachineEdge edge, Actor edgeActor) {
        cardActorParent.getCardChildren().remove(this);
        cardActorParent = newParentCard;
        cardActorParent.getCardChildren().add(this);

        data.setParent(edge);
        validate();
        Vector2 stageCoordinate = edgeActor.localToStageCoordinates(new Vector2(0, -80));
        setPosition(stageCoordinate.x, stageCoordinate.y);
        toFront();
    }

    @Override
    public List<ActorMachineCard> getCardChildren() {
        return cardActorChild;
    }

    public void moveByR(float x, float y) {
        for (ActorMachineCard cardChild : getCardChildren()) {
            cardChild.moveByR(x, y);
        }
        toFront();
        moveBy(x, y);
    }
}
