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
import javafx.application.Platform;
import uicheckers.uiGame.UIGame;

import java.util.List;


public class Game extends Thread {

    private Table table;
    private PieceColor playerTurn = PieceColor.WHITE;
    private List<Player> players;
    private UIGame uiGame;
    private int nrUnchangedState = 0;
    private int nrUnchagedPieces = 24;
    private  int nrOfTies = 0;
    public final static int NR_OF_GAMES = 5;


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

    public void reInitBoard(){
        this.table = Actions.newGame();
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                Piece piece = table.getTable()[i][j].getPiece();
                if (uiGame.getBoard()[j][7-i].getUIPiece() != null){
                    uiGame.getBoard()[j][7-i].getUIPiece().setPiece(piece);
                }
            }
        }
        playerTurn = PieceColor.WHITE;
    }

    public void play() throws InterruptedException {
        int nrGame = 0;
        while (nrGame < NR_OF_GAMES){
            Printer.printTable(table);
            while (getPlayer() == null || !Actions.finishedGame(table, this)){
                System.out.println(playerTurn.stringValue() + " turn!!!");
                sleep(1000);
                Player player = getPlayer();
                Move move = player.getNextMove(table);

                this.table = player instanceof  HumanPlayer ? GameMoves.makeOneMove(table, move):
                        GameMoves.makeOneMove(table, move, uiGame);
                uiGame.setLastMove(move);
                changeTurn();
                Printer.printTable(table);
            }
            System.out.println("Finished game!!!");
            PieceColor playerColor = Actions.wonGame(table);
            System.out.println(playerColor != null ? "The winner is " + Actions.wonGame(table).stringValue() + " player!!!" :
                    "The game is finished with a tie!");
            if (playerColor == null){
                nrOfTies++;
            }else{
                addWinToPlayer(playerColor);
            }
            players.stream().forEach(player -> {
                System.out.println(player.getNrOfWinsForPlayer());
            });
            System.out.println("Number of ties: " + nrOfTies);
            nrGame++;
            reInitGame(uiGame);
        }

    }

    private void addWinToPlayer(PieceColor color){
        players.stream().forEach(player -> {
            if (player.getPieceColor() == color){
                player.setNrOfWins(player.getNrOfWins() + 1);
            }
        });
    }

    private void reInitGame(UIGame uiGame) throws InterruptedException {
        initUIGame(uiGame);
        sleep(5000);
        reInitBoard();

    }

    public static void initUIGame(UIGame uiGame){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                uiGame.initRootPane();
            }
        });
    }

    public Player getPlayer(){
        return players.stream().filter(player -> player.getPieceColor() == playerTurn).findFirst().orElse(null);
    }

    public void changeTurn(){
        playerTurn = playerTurn.oppositeColor();
    }

    public int getNrUnchangedState() {
        return nrUnchangedState;
    }

    public void setNrUnchangedState(int nrUnchangedState) {
        this.nrUnchangedState = nrUnchangedState;
    }

    public int getNrUnchagedPieces() {
        return nrUnchagedPieces;
    }

    public void setNrUnchagedPieces(int nrUnchagedPieces) {
        this.nrUnchagedPieces = nrUnchagedPieces;
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
