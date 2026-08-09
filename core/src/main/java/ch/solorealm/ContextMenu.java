package ch.solorealm;

import ch.solorealm.beans.levels.Level1;
import ch.solorealm.beans.levels.Level2;
import ch.solorealm.beans.levels.LevelGenerator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ContextMenu {
    private final Context context;

    public ContextMenu(Context context) {
        this.context = context;
    }

    public void createMenu() {
        context.stage.getActors().clear();

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setBackground(new Image(context.assetManager.get("bg/Chapiter_1.png", Texture.class)).getDrawable());
        context.stage.addActor(rootTable);

        Table chapterPanel = createChapterPanel();

        rootTable.add(chapterPanel).left().padLeft(40).expandY();
        rootTable.add().expandX();
    }

    private Table createChapterPanel() {
        Table chapterPanel = new Table();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 0,0, 0.85f));
        pixmap.fill();

        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();
        chapterPanel.setBackground(backgroundDrawable);

        Label titleLabel = new Label("TIER I", context.skin, "window");
        Label subtitleLabel = new Label("And so, it Begins", context.skin, "window");

        Image divider = getDivider();

        chapterPanel.add(titleLabel).padTop(20).row();
        chapterPanel.add(subtitleLabel).padBottom(20).row();
        chapterPanel.add(divider).fillX().height(1).padBottom(20).row();
        chapterPanel.padLeft(20).padRight(20).padBottom(20);

        chapterPanel.add(createLevelRow(new Level1())).row();
        chapterPanel.add(createLevelRow(new Level2())).row();
        return chapterPanel;
    }

    private Table createLevelRow(LevelGenerator levelGenerator) {
        Table row = new Table();
        Image icon = new Image(context.assetManager.get(levelGenerator.iconTexture, Texture.class));
        Label label = new Label(levelGenerator.title, context.skin, "window");

        Image divider = getDivider();

        row.add(icon).size(32, 32).padRight(100);
        row.add(label);
        row.row();
        row.add(divider).fillX().height(1).colspan(2).padBottom(10).row();

        row.setTouchable(Touchable.enabled);
        row.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if (pointer == -1 && (fromActor == null || !fromActor.isDescendantOf(event.getListenerActor()))) {
                    context.soundsManager.playMenuFocus();
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                context.stage.addAction(Actions.sequence(
                    Actions.run(context.soundsManager::playEnterLevel),
                    fadeTransition(true, false),
                    Actions.parallel(
                        Actions.run(() -> context.setLevel(levelGenerator)),
                        fadeTransition(false, true)
                    )
                ));
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        return row;
    }

    public Action fadeTransition(boolean fadeIn, boolean removeImgAfter) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        Image fadeImg = new Image(new TextureRegionDrawable(new TextureRegion(texture)));
        fadeImg.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (fadeIn) {
            fadeImg.getColor().a = 0f;
        } else {
            fadeImg.getColor().a = 1f;
        }
        Action fadeAction;
        if (fadeIn) {
            fadeAction = Actions.fadeIn(0.5f);
        } else {
            fadeAction = Actions.fadeOut(0.5f);
        }
        fadeAction.setTarget(fadeImg);
        SequenceAction sequence = Actions.sequence(Actions.run(() -> context.stage.addActor(fadeImg)), fadeAction);
        if (removeImgAfter) {
            return Actions.sequence(sequence, Actions.run(fadeImg::remove));
        }
        return sequence;
    }

    private static Image getDivider() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GRAY);
        pixmap.fill();

        TextureRegionDrawable dividerDrawable = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();
        Image divider = new Image(dividerDrawable);
        return divider;
    }
}
