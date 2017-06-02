package chekers;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CheckersApp extends Application {
    public static final int TILE_SIZE = 80;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();
    private Tile[][] board = new Tile[WIDTH][HEIGHT];


    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("CheckersApp");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        root.getChildren().addAll(tileGroup, pieceGroup);

        for (int y = 0; y < HEIGHT; y++ ){
            for (int x = 0; x < WIDTH; x++){
                Tile tile  = new Tile((x+y) % 2 == 0, x, y);
                board[x][y] = tile;
                tileGroup.getChildren().add(tile);

                Piece piece = null;
                if (y <= 2 && (x+y)%2 != 0){
                    piece = makePiece(PieceType.BLACK, x, y);

                }

                if (y >= 5 && (x+y)%2 != 0){
                    piece = makePiece(PieceType.WHITE, x, y);

                }
                if (piece != null){
                    tile.setPiece(piece);
                    pieceGroup.getChildren().add(piece);
                }


            }
        }

        return root;
    }

    private Piece makePiece(PieceType type, int x, int y ){
        Piece piece = new Piece(type, x, y);
        return piece;
    }

    public static void main(String[] args){
        launch(args);
    }
}
