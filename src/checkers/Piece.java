package checkers;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;


import java.util.ArrayList;
import java.util.List;


public class Piece extends StackPane {

    private PieceType type;
    private double mouseX, mouseY;
    private double oldX, oldY;
    private boolean king;
    private Ellipse kingEllipse;
    private Ellipse kingBg;



    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }

    public Piece(PieceType type, int x, int y){
        this.type = type;
        move(x , y);
        this.king = false;



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


        kingBg = new Ellipse(CheckersApp.TILE_SIZE * 0.15, CheckersApp.TILE_SIZE * 0.13);
        kingBg.setFill(Color.BLACK);

        kingBg.setStroke(Color.BLACK);
        kingBg.setStrokeWidth(CheckersApp.TILE_SIZE * 0.03);
        kingBg.setTranslateX((CheckersApp.TILE_SIZE - CheckersApp.TILE_SIZE * 0.3125 * 2) / 2 );
        kingBg.setTranslateY((CheckersApp.TILE_SIZE - CheckersApp.TILE_SIZE * 0.26 * 2) / 2 + CheckersApp.TILE_SIZE * 0.03  );


        kingEllipse = new Ellipse(CheckersApp.TILE_SIZE * 0.15, CheckersApp.TILE_SIZE * 0.13);
        kingEllipse.setFill(type == PieceType.BLACK ? Color.BROWN : Color.WHITE);

        kingEllipse.setStroke(Color.BLACK);
        kingEllipse.setStrokeWidth(CheckersApp.TILE_SIZE * 0.03);
        kingEllipse.setTranslateX((CheckersApp.TILE_SIZE - CheckersApp.TILE_SIZE * 0.3125 * 2) / 2 );
        kingEllipse.setTranslateY((CheckersApp.TILE_SIZE - CheckersApp.TILE_SIZE * 0.26 * 2) / 2 );



        kingBg.setVisible(false);
        kingEllipse.setVisible(false);

        getChildren().addAll(bg, ellipse, kingBg, kingEllipse);
        setOnMousePressed(e ->{
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        } );

        setOnMouseDragged(e ->{
            relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY );
        } );


    }

    public void verifyIfKing(int y){
        if (this.type == PieceType.BLACK && y == CheckersApp.HEIGHT-1){
            this.setKing(true);
        }

        if (this.type == PieceType.WHITE && y == 0){
            this.setKing(true);
        }
    }



    public boolean isKing() {
        return king;
    }

    public void setKing(boolean king) {
        if (king){
            this.kingBg.setVisible(true);
            this.kingEllipse.setVisible(true);
        }
        this.king = king;
    }

    public double getMouseX() {
        return mouseX;
    }

    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    public double getOldX() {
        return oldX;
    }

    public void setOldX(double oldX) {
        this.oldX = oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public void setOldY(double oldY) {
        this.oldY = oldY;
    }

    public void move(int x, int y){
        oldX = x * CheckersApp.TILE_SIZE;
        oldY = y * CheckersApp.TILE_SIZE;
        relocate(oldX, oldY);
    }

    public void abortMove(){
        relocate(oldX, oldY);
    }



}
