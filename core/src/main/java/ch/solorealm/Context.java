package ch.solorealm;

import ch.solorealm.beans.levels.LevelGenerator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

public class Context implements Disposable {
    public final AssetManager assetManager;
    public final Skin skin;
    public final Stage stage;
    public final ContextWrk contextWrk;
    public final SoundsManager soundsManager;
    public final ContextMenu contextMenu;

    public Context(Stage stage) {
        assetManager = new AssetManager();
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        this.stage = stage;
        contextWrk = new ContextWrk(this);
        soundsManager = new SoundsManager(this);
        contextMenu = new ContextMenu(this);
    }

    public void initializeGame() {
        loadAsset();
        contextMenu.createMenu();
    }

    public void loadAsset() {
        FileHandle assets = Gdx.files.internal("assets.txt");
        for (String assetPath : assets.readString().split("\n")) {
            if (assetPath.startsWith("ui")) {
                continue;
            }

            Class<?> fileType;
            if (assetPath.startsWith("sounds")) {
                fileType = Sound.class;
            } else {
                fileType = Texture.class;
            }
            assetManager.load(assetPath, fileType);
        }
        assetManager.finishLoading();
    }

    public void setLevel(LevelGenerator levelGenerator) {
        contextWrk.createGrid(levelGenerator);
    }

    public static Runnable onlyEdgeTrigger(BooleanSupplier test, Runnable run) {
        AtomicBoolean old = new AtomicBoolean(test.getAsBoolean());
        return () -> {
            boolean newTest = test.getAsBoolean();
            if (!old.get() && newTest) {
                run.run();
                old.set(true);
            }
        };
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
