package ch.solorealm;

import com.badlogic.gdx.audio.Sound;

public class SoundsManager {
    private final Context context;

    public SoundsManager(Context context) {
        this.context = context;
    }

    public void playProcess() {
        Sound sound = context.assetManager.get("sounds/exp.mp3", Sound.class);
        sound.play();
    }

    public void playCardDragStart() {
        Sound sound = context.assetManager.get("sounds/cloth2.mp3", Sound.class);
        sound.play();
    }

    public void playCardDragDrop() {
        Sound sound = context.assetManager.get("sounds/cloth3.mp3", Sound.class);
        sound.play();
    }

    public void playIngredientDragStart() {
        Sound sound = context.assetManager.get("sounds/stone1.mp3", Sound.class);
        sound.play();
    }

    public void playIngredientDragDrop() {
        Sound sound = context.assetManager.get("sounds/stone2.mp3", Sound.class);
        sound.play();
    }
}
