package alghoritms.players.minmax;



import alghoritms.actions.Move;
import alghoritms.model.PieceColor;
import alghoritms.model.environment.Table;

import java.util.ArrayList;
import java.util.List;

public class MinMaxNode {
    private PieceColor playerPieceColor;
    private int value;
    private List<MinMaxNode> childrenNodes = new ArrayList<>();
    private boolean isLeaf;
    private Table state;
    private Move move;

    public MinMaxNode(PieceColor playerPieceColor, boolean isLeaf, Table state, Move move) {
        this.playerPieceColor = playerPieceColor;
        this.isLeaf = isLeaf;
        this.state = state;
        this.move = move;
    }



    public int getValue() {
        return value;
    }

    public PieceColor getPlayerPieceColor() {
        return playerPieceColor;
    }

    public void setPlayerPieceColor(PieceColor playerPieceColor) {
        this.playerPieceColor = playerPieceColor;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public List<MinMaxNode> getChildrenNodes() {
        return childrenNodes;
    }

    public void setChildrenNodes(List<MinMaxNode> childrenNodes) {
        this.childrenNodes = childrenNodes;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public Table getState() {
        return state;
    }

    public void setState(Table state) {
        this.state = state;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }
}
