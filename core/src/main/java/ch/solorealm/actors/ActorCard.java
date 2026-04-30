package ch.solorealm.actors;

import ch.solorealm.beans.IngredientCard;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorCard extends Actor {
    private final IngredientCard ingredientCard;
    private String oldAssetName;
    private Texture texture;

    public ActorCard(IngredientCard ingredientCard) {
        this.ingredientCard = ingredientCard;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        String assetName = ingredientCard.getAssetName();
        if (!assetName.equals(oldAssetName)) {
            texture = new Texture(assetName);
        }
        oldAssetName = assetName;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());
    }
}
