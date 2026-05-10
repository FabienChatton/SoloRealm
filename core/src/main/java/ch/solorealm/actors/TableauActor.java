package ch.solorealm.actors;

import ch.solorealm.beans.Tableau;
import ch.solorealm.beans.machine.RootMachine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class TableauActor extends Table {
    public final Tableau data;

    public TableauActor(Tableau data, Skin skin, Texture backgroundTextureRoot) {
        this.data = data;
        HorizontalGroup hGroup = new HorizontalGroup();
        hGroup.space(32);
        for (int i = 0; i < data.rootNodes.length; i++) {
            hGroup.addActor(new RootActor(skin, new RootMachine(), backgroundTextureRoot));
        }
        add(hGroup);
    }
}
