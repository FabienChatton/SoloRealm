package ch.solorealm;

import ch.solorealm.actors.ActorIngredientCard;
import ch.solorealm.actors.ActorMachineCard;
import ch.solorealm.actors.TableauActor;
import ch.solorealm.beans.Tableau;
import ch.solorealm.beans.ingredient.CopperIngredient;
import ch.solorealm.beans.ingredient.IngredientType;
import ch.solorealm.beans.machine.FurnaceMachine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
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

        AssetManager assetManager = new AssetManager();
        assetManager.load("cards/empty_card.png", Texture.class);
        assetManager.load("ingredients/copper_ingot.png", Texture.class);
        assetManager.load("machines/furnace.png", Texture.class);
        assetManager.load("machines/Grid_Overclocker_Upgrade.png", Texture.class);
        assetManager.load("cards/empty_root.png", Texture.class);
        assetManager.finishLoading();

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Tableau tableau = new Tableau(6);

        CopperIngredient copperData = new CopperIngredient(IngredientType.INGOT);
        ActorIngredientCard testActor = new ActorIngredientCard(copperData, assetManager.get(copperData.getAssetName()), assetManager.get("cards/empty_card.png"));

        FurnaceMachine furnaceData = new FurnaceMachine();
        furnaceData.setParent(tableau.rootNodes[0]);
        ActorMachineCard actorMachineCard = new ActorMachineCard(skin, furnaceData, assetManager.get(furnaceData.getAssetName()), assetManager.get("cards/empty_card.png"), assetManager.get("machines/Grid_Overclocker_Upgrade.png"));

        actorMachineCard.setPosition(1058.0f,328.5f);

        TableauActor tableauActor = new TableauActor(tableau, skin, assetManager.get("cards/empty_root.png"));
        tableauActor.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);

        stage.setDebugAll(true);
        stage.addActor(testActor);
        stage.addActor(actorMachineCard);
        stage.addActor(tableauActor);
        actorMachineCard.toFront();
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
