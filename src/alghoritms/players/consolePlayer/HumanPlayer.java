package alghoritms.players.consolePlayer;

import alghoritms.actions.Move;
import alghoritms.model.PieceColor;
import alghoritms.model.environment.Table;
import alghoritms.players.Player;
import uicheckers.uiGame.UIGame;

import static java.lang.Thread.sleep;

public class HumanPlayer extends Player {
    public HumanPlayer(PieceColor pieceColor) {
        super(pieceColor);
    }

    @Override
    public Move getNextMove(Table currentState) {
        System.out.println("expecting human player");
        while(UIGame.getTurn() == getPieceColor()){
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("white made a move");
        return UIGame.getLastMove();
    }
}
