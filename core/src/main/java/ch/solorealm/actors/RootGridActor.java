package ch.solorealm.actors;

import ch.solorealm.beans.RootGrid;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class RootGridActor extends Table {
    public final RootGrid data;
    public final RootActor[] rootActors;

    public RootGridActor(RootGrid data, Skin skin, Texture backgroundTextureRoot) {
        this.data = data;
        HorizontalGroup hGroup = new HorizontalGroup();
        hGroup.space(32);
        rootActors = new RootActor[data.rootNodes.length];
        for (int i = 0; i < data.rootNodes.length; i++) {
            RootActor rootActor = new RootActor(skin, data.rootNodes[i], backgroundTextureRoot);
            hGroup.addActor(rootActor);
            rootActors[i] = rootActor;
        }
        add(hGroup);
    }
}
