package chekers;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;




public class Piece extends StackPane {

    private PieceType type;

    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }

    public Piece(PieceType type, int x, int y){
        this.type = type;
        relocate(x * CheckersApp.TILE_SIZE, y * CheckersApp.TILE_SIZE);

        Ellipse bg = new Ellipse(CheckersApp.TILE_SIZE * 0.3125, CheckersApp.TILE_SIZE * 0.26);
        bg.setFill(Color.BLACK);

        bg.setStroke(Color.BLACK);
        bg.setStrokeWidth(CheckersApp.TILE_SIZE * 0.03);
        bg.setTranslateX((CheckersApp.TILE_SIZE - CheckersApp.TILE_SIZE * 0.3125 * 2) / 2 );
        bg.setTranslateY((CheckersApp.TILE_SIZE - CheckersApp.TILE_SIZE * 0.26 * 2) / 2 + CheckersApp.TILE_SIZE * 0.07  );


        Ellipse ellipse = new Ellipse(CheckersApp.TILE_SIZE * 0.3125, CheckersApp.TILE_SIZE * 0.26);
        ellipse.setFill(type == PieceType.BLACK ? Color.BROWN : Color.WHITE);

        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(CheckersApp.TILE_SIZE * 0.03);
        ellipse.setTranslateX((CheckersApp.TILE_SIZE - CheckersApp.TILE_SIZE * 0.3125 * 2) / 2 );
        ellipse.setTranslateY((CheckersApp.TILE_SIZE - CheckersApp.TILE_SIZE * 0.26 * 2) / 2 );



        getChildren().addAll(bg, ellipse);
    }
}
