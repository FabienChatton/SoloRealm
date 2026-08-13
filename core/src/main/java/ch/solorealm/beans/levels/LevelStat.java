package ch.solorealm.beans.levels;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public final class LevelStat {
    private final Label nbrMoveLabel;
    private final Label nbrProcessLabel;
    private final Label nbrCardLabel;

    public LevelStat(Label nbrMoveLabel, Label nbrProcessLabel, Label nbrCardLabel) {
        this.nbrMoveLabel = nbrMoveLabel;
        this.nbrProcessLabel = nbrProcessLabel;
        this.nbrCardLabel = nbrCardLabel;
        setMoveClk(0);
        setNbrProcess(0);
        setNbrCard(0);
    }

    public void setMoveClk(int nbr) {
        nbrMoveLabel.setText("" + nbr);
    }

    public void nbrMovePlus1() {
        setMoveClk(Integer.parseInt(nbrMoveLabel.getText().toString()) + 1);
    }

    public void setNbrProcess(int nbr) {
        nbrProcessLabel.setText("" + nbr);
    }

    public void nbrProcessPlus1() {
        setNbrProcess(Integer.parseInt(nbrProcessLabel.getText().toString()) + 1);
    }

    public void setNbrCard(int nbr) {
        nbrCardLabel.setText("" + nbr);
    }

    public void nbrCardPlus1() {
        setNbrCard(Integer.parseInt(nbrCardLabel.getText().toString()) + 1);
    }
}
