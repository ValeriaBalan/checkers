package alghoritms.actions;



import alghoritms.model.agent.Piece;

import java.util.ArrayList;
import java.util.List;

public class Move {
    private Piece piece;
    private List<Action> actions = new ArrayList<>();
    private Boolean attack = false;



    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Boolean getAttack() {
        return attack;
    }

    public void setAttack(Boolean attack) {
        this.attack = attack;
    }
}
