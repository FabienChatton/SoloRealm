package ch.solorealm;

import ch.solorealm.actors.ActorCard;
import ch.solorealm.beans.CopperIngredient;
import ch.solorealm.beans.IngredientType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private Stage stage;
    private Skin skin;

    @Override
    public void create() {
        stage = new Stage(new FitViewport(1488, 837));
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        ActorCard testActor = new ActorCard(new CopperIngredient(IngredientType.INGOT), new Texture("cards/empty_card.png"));

        stage.addActor(testActor);
        stage.setDebugAll(true);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
