package alghoritms.players;




import alghoritms.actions.Move;
import alghoritms.model.PieceColor;
import alghoritms.model.agent.Piece;
import alghoritms.model.environment.Table;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private List<Piece> currentPieces = new ArrayList<>();
    private final PieceColor pieceColor;

    public abstract Move getNextMove(Table currentState);

    public Player(PieceColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    public List<Piece> getCurrentPieces() {
        return currentPieces;
    }

    public void setCurrentPieces(List<Piece> currentPieces) {
        this.currentPieces = currentPieces;
    }

    public PieceColor getPieceColor() {
        return pieceColor;
    }

}
