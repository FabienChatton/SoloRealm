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

    public void playValidatedFoundation() {
        Sound sound = context.assetManager.get("sounds/exp-multi.mp3", Sound.class);
        sound.play();
    }

    public void playEnterLevel() {
        Sound sound = context.assetManager.get("sounds/mm_join.wav", Sound.class);
        long play = sound.play();
        sound.setVolume(play, 0.5f);
    }

    public void playMenuFocus() {
        Sound sound = context.assetManager.get("sounds/menu_focus.wav", Sound.class);
        long play = sound.play();
        sound.setVolume(play, 0.5f);
    }

    public void playEndLevel() {
        Sound sound = context.assetManager.get("sounds/achievement_earned.wav", Sound.class);
        long play = sound.play();
        sound.setVolume(play, 0.5f);
    }
}
