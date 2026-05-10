package ch.solorealm.actors;

import ch.solorealm.beans.machine.MachineEdge;
import ch.solorealm.beans.machine.MachineNode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

public class ActorMachineCard extends Table {
    public final MachineNode data;
    private final Image backgroundImage;
    private final Image icon;
    private static final float SCALE_FACTOR = 1.5f;

    public ActorMachineCard(Skin skin, MachineNode data, Texture iconTexture, Texture backgroundTexture, Texture arrowTexture) {
        this.data = data;
        this.backgroundImage = new Image(backgroundTexture);
        this.icon = new Image(iconTexture);

        float scaledWidth = backgroundTexture.getWidth() * SCALE_FACTOR;
        float scaledHeight = backgroundTexture.getHeight() * SCALE_FACTOR;
        this.setSize(scaledWidth, scaledHeight);

        Stack stack = new Stack();
        stack.add(this.backgroundImage);

        Table contentTable = new Table();
        Table edgesTable = new Table();

        for (int i = 0; i < data.edges.length; i++) {
            MachineEdge edge = data.edges[i];
            Table edgeTable = new Table();

            float arrowW = arrowTexture.getWidth() * SCALE_FACTOR;
            float arrowH = arrowTexture.getHeight() * SCALE_FACTOR;

            if (edge.inputType != null) {
                edgeTable.add(new Image(arrowTexture)).size(arrowW, arrowH);
            }
            if (edge.inputType != null) {
                Image arrowImage = new Image(arrowTexture);
                arrowImage.setOrigin(arrowW / 2f, arrowH / 2f);
                arrowImage.rotateBy(180);
                arrowImage.setAlign(Align.center);
                edgeTable.add(arrowImage).size(arrowW, arrowH);
            }
            edgesTable.add(edgeTable);
        }

        contentTable.add(edgesTable).row();

        Label machineNameLabel = new Label(data.getMachineName(), skin, "window");
        machineNameLabel.setColor(Color.BLACK);
        machineNameLabel.setFontScale(SCALE_FACTOR);

        contentTable.add(machineNameLabel).center().pad(5 * SCALE_FACTOR).row();
        contentTable.add(icon).size(64 * SCALE_FACTOR, 64 * SCALE_FACTOR).expandY().row();
        stack.add(contentTable);
        this.add(stack).width(scaledWidth).height(scaledHeight);
    }
}
