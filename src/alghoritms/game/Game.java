package alghoritms.game;



import alghoritms.actions.Actions;
import alghoritms.actions.GameMoves;
import alghoritms.actions.Move;
import alghoritms.model.PieceColor;
import alghoritms.model.agent.Piece;
import alghoritms.model.environment.Table;
import alghoritms.players.Player;
import alghoritms.players.consolePlayer.HumanPlayer;
import alghoritms.print.Printer;
import checkers.uiGame.UIGame;

import java.util.List;

import static java.lang.Thread.sleep;

public class Game extends Thread {

    private Table table;
    private PieceColor playerTurn = PieceColor.WHITE;
    private List<Player> players;
    private UIGame uiGame;


    public Game(List<Player> players, UIGame uiGame) {
        this.players = players;
        this.table = Actions.newGame();
        this.uiGame = uiGame;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                Piece piece = table.getTable()[i][j].getPiece();
                if (uiGame.getBoard()[j][7-i].getUIPiece() != null){
                    uiGame.getBoard()[j][7-i].getUIPiece().setPiece(piece);
                }
            }
        }
    }

    public void play() throws InterruptedException {
        Printer.printTable(table);
        while (getPlayer() == null || !Actions.finishedGame(table)){
            System.out.println(playerTurn.stringValue() + " turn!!!");
            Player player = getPlayer();
            Move move = player.getNextMove(table);

            this.table = player instanceof  HumanPlayer ? GameMoves.makeOneMove(table, move):
                    GameMoves.makeOneMove(table, move, uiGame);
            uiGame.setLastMove(move);
            changeTurn();

            Printer.printTable(table);
        }
        System.out.println("Finished game!!!");
        System.out.println("The winner is " + Actions.wonGame(table).stringValue() + " player!!!");
    }

    public Player getPlayer(){
        return players.stream().filter(player -> player.getPieceColor() == playerTurn).findFirst().orElse(null);
    }

    public void changeTurn(){
        playerTurn = playerTurn.oppositeColor();
    }

    @Override
    public void run() {
        try {
            play();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
