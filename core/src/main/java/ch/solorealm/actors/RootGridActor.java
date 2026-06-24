package ch.solorealm.actors;

import ch.solorealm.beans.RootGrid;
import ch.solorealm.beans.machine.RootMachine;
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
            RootActor rootActor = new RootActor(skin, new RootMachine(), backgroundTextureRoot);
            hGroup.addActor(rootActor);
            rootActors[i] = rootActor;
        }
        add(hGroup);
    }

    public boolean isDropValide(int rootActorIndex, ActorMachineCard machineCard) {
        int cardWith = machineCard.data.edges.length;
        for (int i = rootActorIndex; i < rootActorIndex + cardWith; i++) {
            if (i >= rootActors.length) {
                return false;
            }
            if (!rootActors[i].getCardChildren().contains(machineCard)
                && !rootActors[i].getCardChildren().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
