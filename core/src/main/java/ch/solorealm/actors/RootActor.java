package ch.solorealm.actors;

import ch.solorealm.beans.machine.RootMachine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RootActor extends Table {
    public final RootMachine data;
    private final Image backgroundImage;

    public RootActor(Skin skin, RootMachine data, Texture backgroundTexture) {
        this.data = data;
        this.backgroundImage = new Image(backgroundTexture);

        setSize(backgroundTexture.getWidth() * 1.5f, backgroundTexture.getHeight() * 1.5f);

        Stack stack = new Stack();
        stack.add(backgroundImage);

        add(stack).size(backgroundTexture.getWidth() * 1.5f, backgroundTexture.getHeight() * 1.5f);

        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println(localToStageCoordinates(new Vector2(0, 0)));
                return true;
            }
        });
    }
}
