package ch.solorealm.actors;

import ch.solorealm.beans.machine.MachineEdge;
import ch.solorealm.beans.machine.MachineNode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class ActorMachineCard extends Table {
    public final MachineNode data;
    private final Image backgroundImage;
    private final Image icon;

    public ActorMachineCard(Skin skin, MachineNode data, Texture iconTexture, Texture backgroundTexture, Texture arrowTexture) {
        this.data = data;
        this.backgroundImage = new Image(backgroundTexture);
        this.icon = new Image(iconTexture);

        setTransform(true);
        setScale(1.5f);
        setSize(backgroundTexture.getWidth(), backgroundTexture.getHeight());

        Stack stack = new Stack();
        stack.add(this.backgroundImage);

        Table table = new Table();
        Table edgesTable = new Table();
        for (int i = 0; i < data.edges.length; i++) {
            MachineEdge edge = data.edges[i];
            Table edgeTable = new Table();
            if (edge.inputType != null) {
                edgeTable.add(new Image(arrowTexture));
            }
            if (edge.inputType != null) {
                Image arrowImage = new Image(arrowTexture);
                arrowImage.setOrigin(arrowTexture.getWidth() / 2f, arrowTexture.getHeight() / 2f);
                arrowImage.rotateBy(180);
                edgeTable.add(arrowImage);
            }
            edgesTable.add(edgeTable);
        }
        table.add(edgesTable).row();
        Label machineNameLabel = new Label(data.getMachineName(), skin, "window");
        machineNameLabel.setColor(Color.BLACK);
        table.add(machineNameLabel).center().row();
        table.add(icon).size(64, 64).expandY().row();

        stack.add(table);

        this.add(stack);
    }
}
