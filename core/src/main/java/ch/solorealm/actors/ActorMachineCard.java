package ch.solorealm.actors;

import ch.solorealm.beans.machine.MachineNode;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ActorMachineCard extends Table {
    public final MachineNode data;
    private final Image backgroundImage;
    private final Image icon;

    public ActorMachineCard(MachineNode data, Texture iconTexture, Texture backgroundTexture, Texture arrowTexture) {
        this.data = data;
        this.backgroundImage = new Image(backgroundTexture);
        this.icon = new Image(iconTexture);

        setTransform(true);
        setScale(1.5f);
        setSize(backgroundTexture.getWidth(), backgroundTexture.getHeight());

        Stack stack = new Stack();
        stack.add(this.backgroundImage);

        Table iconTable = new Table();
        iconTable.add(icon).center();
        stack.add(iconTable);

        this.add(stack).fill().expand();
    }
}
