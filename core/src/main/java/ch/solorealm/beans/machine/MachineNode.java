package ch.solorealm.beans.machine;

import ch.solorealm.beans.GetAssetResource;
import ch.solorealm.beans.ingredient.IngredientCard;
import ch.solorealm.beans.ingredient.IngredientType;

public abstract class MachineNode implements GetAssetResource {
    private IngredientCard input;
    private IngredientCard output;
    private MachineNode parent;

    private final IngredientType inputType;
    private final IngredientType outputType;

    public MachineNode(IngredientType inputType, IngredientType outputType) {
        this.inputType = inputType;
        this.outputType = outputType;
    }

    protected abstract String getMachineName();

    @Override
    public String getAssetName() {
        return String.format("machines/%s.png", getMachineName());
    }
}
