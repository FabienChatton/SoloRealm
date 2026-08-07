package ch.solorealm;

import ch.solorealm.beans.levels.LevelGenerator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
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
        assetManager.load("cards/empty_card.png", Texture.class);
        assetManager.load("ingredients/copper_ingot.png", Texture.class);
        assetManager.load("ingredients/copper_ore.png", Texture.class);
        assetManager.load("ingredients/iron_ingot.png", Texture.class);
        assetManager.load("ingredients/bronze_ingot.png", Texture.class);
        assetManager.load("ingredients/tin_ingot.png", Texture.class);
        assetManager.load("ingredients/tin_ore.png", Texture.class);
        assetManager.load("machines/Furnace.png", Texture.class);
        assetManager.load("machines/Grid_Overclocker_Upgrade.png", Texture.class);
        assetManager.load("cards/empty_root.png", Texture.class);
        assetManager.load("machines/Assembling_Machine.png", Texture.class);
        assetManager.load("machines/Alloy_Smelter.png", Texture.class);
        assetManager.load("machines/Trash.png", Texture.class);
        assetManager.load("sounds/cloth2.mp3", Sound.class);
        assetManager.load("sounds/cloth3.mp3", Sound.class);
        assetManager.load("sounds/exp.mp3", Sound.class);
        assetManager.load("sounds/stone1.mp3", Sound.class);
        assetManager.load("sounds/stone2.mp3", Sound.class);
        assetManager.load("sounds/exp-multi.mp3", Sound.class);
        assetManager.load("sounds/mm_join.wav", Sound.class);
        assetManager.load("sounds/menu_focus.wav", Sound.class);
        assetManager.load("sounds/achievement_earned.wav", Sound.class);
        assetManager.load("bg/bg.jpg", Texture.class);
        assetManager.load("bg/Chapiter_1.png", Texture.class);
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
