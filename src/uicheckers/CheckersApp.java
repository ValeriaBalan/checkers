package uicheckers;

import alghoritms.game.Game;
import alghoritms.model.PieceColor;
import alghoritms.players.Player;
import alghoritms.players.consolePlayer.HumanPlayer;
import alghoritms.players.minmax.MinMaxAlgorithmPlayer;
import alghoritms.players.neuralNetwokPlayer.NeuralNetworkPlayer;
import uicheckers.uiGame.UIGame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Arrays;

import static java.lang.Thread.sleep;


public class CheckersApp extends Application{
    private static UIGame uiGame;
    private int nrMaxOfGames = 5;

    @Override
    public void start(Stage primaryStage) throws Exception {
        uiGame = new UIGame();
        Scene scene = new Scene(uiGame.createContent());
        primaryStage.setTitle("CheckersApp");
        primaryStage.setScene(scene);
        primaryStage.show();
        uiGame.setTurn(PieceColor.WHITE);
        playGame();
    }

    public  void playGame(){
        Player whitePlayer = new MinMaxAlgorithmPlayer(PieceColor.WHITE);
        Player blackPlayer = new NeuralNetworkPlayer(PieceColor.BLACK);
        Game game = new Game(Arrays.asList(whitePlayer, blackPlayer), uiGame);
        game.start();
    }

    public static void main(String[] args){
        launch(args);

    }



}
