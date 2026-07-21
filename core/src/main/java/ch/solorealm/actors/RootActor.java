package ch.solorealm.actors;

import ch.solorealm.beans.machine.RootMachine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.List;

public class RootActor extends Table implements GetCardChildren {
    public final RootMachine data;
    public final Image backgroundImage;
    private final List<ActorMachineCard> cardChildren;

    public RootActor(RootMachine data, Texture backgroundTexture) {
        this.data = data;
        this.backgroundImage = new Image(backgroundTexture);
        this.cardChildren = new ArrayList<>();

        setSize(backgroundTexture.getWidth() * 1.5f, backgroundTexture.getHeight() * 1.5f);

        Stack stack = new Stack();
        stack.add(backgroundImage);

        add(stack).size(backgroundTexture.getWidth() * 1.5f, backgroundTexture.getHeight() * 1.5f);

        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
    }

    @Override
    public List<ActorMachineCard> getCardChildren() {
        return cardChildren;
    }
}
