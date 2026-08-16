package ch.solorealm;

import ch.solorealm.beans.levels.LevelStat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.madgag.gif.fmsware.AnimatedGifEncoder;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ContextLevelEnd {
    private final Context context;
    private final String levelName;
    private LevelStat levelStat;
    private Table statTable;
    private Image currenScreenShot;
    private int currentImageI;
    private Label saveGifMsg;

    public ContextLevelEnd(Context context, String levelName) {
        this.context = context;
        this.levelName = levelName;
    }

    public void setLevelEnd() {
        context.stage.getActors().clear();

        currenScreenShot = new Image();
        currentImageI = -1;
        nextScreenShot();

        Table table = new Table();
        table.setFillParent(true);
        table.add(currenScreenShot).size(1488  * 0.7f, 837 * 0.7f).row();
        table.add(statTable).padTop(10).row();
        Table bottomRow = new Table();

        TextButton saveToGifButton = new TextButton("Save to gif", context.skin);
        saveToGifButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                saveGif();
                return true;
            }
        });
        saveToGifButton.pad(10);
        saveToGifButton.getLabel().setFontScale(2);
        bottomRow.add(saveToGifButton);

        TextButton goToMenuButton = new TextButton("Go to Menu", context.skin);
        goToMenuButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                levelStat.dispose();
                context.stage.addAction(context.contextMenu.fadeToMenu());
                return true;
            }
        });
        goToMenuButton.pad(10);
        goToMenuButton.getLabel().setFontScale(2);
        bottomRow.add(goToMenuButton).padLeft(30);
        table.add(bottomRow).padTop(10).row();

        saveGifMsg = new Label(null, context.skin);
        table.add(saveGifMsg).height(30).row();

        context.stage.addActor(table);


        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                nextScreenShot();
            }
        }, 0, 0.5f, -1).run();
    }

    private void nextScreenShot() {
        if (levelStat.disposed) return;
        if (currentImageI == levelStat.getProcessScreenShot().size() - 1) {
            currentImageI = 0;
        } else {
            currentImageI++;
        }
        Pixmap pixmap = levelStat.getProcessScreenShot().get(currentImageI);
        TextureRegion region = new TextureRegion(new Texture(pixmap));
        region.flip(false, true);
        currenScreenShot.setDrawable(new TextureRegionDrawable(region));
    }

    private void saveGif() {
        saveGifMsg.setText("Saving the gif...");
        Gdx.app.postRunnable(() -> {
            try {
                AnimatedGifEncoder encoder = new AnimatedGifEncoder();
                String home = System.getProperty("user.home");
                LocalDateTime time = LocalDateTime.now();
                String savePath = String.format("%s/Downloads/SoloRealm-%s-%s.gif", home, levelName, time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-s")));
                encoder.start(savePath);
                encoder.setDelay(500);
                encoder.setRepeat(0);

                for (Pixmap screenshot : levelStat.getProcessScreenShot()) {
                    encoder.addFrame(pixmapToBufferedImage(screenshot));
                }
                encoder.finish();
                saveGifMsg.setText("Gif saved to " + Path.of(savePath).normalize());
            } catch (Exception e) {
                saveGifMsg.setText("Fail to save gif: " + e.getMessage());
            }
        });
    }

    private BufferedImage pixmapToBufferedImage(Pixmap pixmap) {
        int width = pixmap.getWidth();
        int height = pixmap.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixmap.getPixel(x, height - 1 - y);
                int r = (pixel & 0xff000000) >>> 24;
                int g = (pixel & 0x00ff0000) >>> 16;
                int b = (pixel & 0x0000ff00) >>> 8;
                int a = (pixel & 0x000000ff);

                int argb = (a << 24) | (r << 16) | (g << 8) | b;
                bufferedImage.setRGB(x, y, argb);
            }
        }

        return bufferedImage;
    }

    public Table createStatTable() {
        Label nbrMoveLabel = new Label(null, context.skin, "window");
        Label nbrProcessLabel = new Label(null, context.skin, "window");
        Label nbrCardLabel = new Label(null, context.skin, "window");
        Table statTable = new Table();
        statTable.add(new Label("Moves:", context.skin, "window")).left();
        statTable.add(nbrMoveLabel).padLeft(20).row();
        statTable.add(new Label("Process:", context.skin, "window")).left();
        statTable.add(nbrProcessLabel).padLeft(20).row();
        statTable.add(new Label("Cards:", context.skin, "window")).left();
        statTable.add(nbrCardLabel).padLeft(20).row();
        statTable.setPosition(900, 50);
        statTable.pad(15);
        Pixmap levelStatBgColor = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        levelStatBgColor.setColor(0.098f, 0.098f, 0.098f, 1);
        levelStatBgColor.fill();
        TextureRegionDrawable levelStatBg = new TextureRegionDrawable(new Texture(levelStatBgColor));
        levelStatBgColor.dispose();
        statTable.setBackground(levelStatBg);
        statTable.pack();
        levelStat = new LevelStat(nbrMoveLabel, nbrProcessLabel, nbrCardLabel);
        this.statTable = statTable;
        return statTable;
    }

    public LevelStat getLevelStat() {
        return levelStat;
    }
}
