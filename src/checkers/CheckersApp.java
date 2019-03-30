package checkers;

import alghoritms.game.Game;
import alghoritms.model.PieceColor;
import alghoritms.players.Player;
import alghoritms.players.consolePlayer.HumanPlayer;
import alghoritms.players.minmax.MinMaxAlgorithmPlayer;
import checkers.uiGame.UIGame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Arrays;


public class CheckersApp extends Application{
    private static UIGame uiGame;

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

    public  void playGame() throws InterruptedException {
        Player whitePlayer = new HumanPlayer(PieceColor.WHITE);
        Player blackPlayer = new MinMaxAlgorithmPlayer(PieceColor.BLACK);
        Game game = new Game(Arrays.asList(whitePlayer, blackPlayer), uiGame);
        game.start();
//        game.play();
    }

    public static void main(String[] args) throws InterruptedException {
        launch(args);

    }



}
