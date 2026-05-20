package ch.solorealm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

public class Context implements Disposable {
    public final AssetManager assetManager;
    public final Skin skin;
    public final Stage stage;
    public final ContextWrk contextWrk;

    public Context(Stage stage) {
        assetManager = new AssetManager();
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        this.stage = stage;
        contextWrk = new ContextWrk(this);
    }

    public void initializeGame() {
        loadAsset();
        contextWrk.createGrid();
    }

    public void loadAsset() {
        assetManager.load("cards/empty_card.png", Texture.class);
        assetManager.load("ingredients/copper_ingot.png", Texture.class);
        assetManager.load("machines/furnace.png", Texture.class);
        assetManager.load("machines/Grid_Overclocker_Upgrade.png", Texture.class);
        assetManager.load("cards/empty_root.png", Texture.class);
        assetManager.load("machines/Assembling_Machine.png", Texture.class);
        assetManager.finishLoading();

    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
