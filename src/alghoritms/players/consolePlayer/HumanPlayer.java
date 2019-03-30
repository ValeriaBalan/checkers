package alghoritms.players.consolePlayer;

import alghoritms.actions.Move;
import alghoritms.model.PieceColor;
import alghoritms.model.environment.Table;
import alghoritms.players.Player;
import checkers.uiGame.UIGame;

public class HumanPlayer extends Player {
    public HumanPlayer(PieceColor pieceColor) {
        super(pieceColor);
    }

    @Override
    public Move getNextMove(Table currentState) {
        while(UIGame.getTurn() == getPieceColor()){

        }
        System.out.println("white made a move");
        return UIGame.getLastMove();
    }
}
