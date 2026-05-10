package ch.solorealm.actors;

import ch.solorealm.beans.ingredient.IngredientCard;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ActorIngredientCard extends Table {
    public final IngredientCard data;
    private final Image backgroundImage;
    private final Image icon;
    private static final float SCALE_FACTOR = 1.5f;

    public ActorIngredientCard(IngredientCard data, Texture iconTexture, Texture backgroundTexture) {
        this.data = data;
        this.backgroundImage = new Image(backgroundTexture);
        this.icon = new Image(iconTexture);

        setSize(backgroundTexture.getWidth() * SCALE_FACTOR, backgroundTexture.getHeight() * SCALE_FACTOR);

        Stack stack = new Stack();
        stack.add(this.backgroundImage);

        Table iconTable = new Table();
        iconTable.add(icon).size(iconTexture.getHeight() * SCALE_FACTOR, iconTexture.getWidth() * SCALE_FACTOR).center();
        stack.add(iconTable);

        this.add(stack).fill().expand();
    }
}
