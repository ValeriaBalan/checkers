package checkers.model;


import alghoritms.model.PieceColor;
import alghoritms.model.agent.Piece;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import static checkers.uiGame.UIGame.HEIGHT;
import static checkers.uiGame.UIGame.TILE_SIZE;


public class UIPiece extends StackPane {

    private PieceColor type;
    private Piece piece;
    private double mouseX, mouseY;
    private double oldX, oldY;
    private boolean king;
    private Ellipse kingEllipse;
    private Ellipse kingBg;



    public PieceColor getType() {
        return type;
    }

    public void setType(PieceColor type) {
        this.type = type;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public UIPiece(PieceColor type, int x, int y){
        this.type = type;
        move(x , y);
        this.king = false;



        Ellipse bg = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);
        bg.setFill(Color.BLACK);

        bg.setStroke(Color.BLACK);
        bg.setStrokeWidth(TILE_SIZE * 0.03);
        bg.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2 );
        bg.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2 + TILE_SIZE * 0.07  );


        Ellipse ellipse = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);
        ellipse.setFill(type == PieceColor.BLACK ? Color.BROWN : Color.WHITE);

        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(TILE_SIZE * 0.03);
        ellipse.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2 );
        ellipse.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2 );


        kingBg = new Ellipse(TILE_SIZE * 0.15, TILE_SIZE * 0.13);
        kingBg.setFill(Color.BLACK);

        kingBg.setStroke(Color.BLACK);
        kingBg.setStrokeWidth(TILE_SIZE * 0.03);
        kingBg.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2 );
        kingBg.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2 + TILE_SIZE * 0.03  );


        kingEllipse = new Ellipse(TILE_SIZE * 0.15, TILE_SIZE * 0.13);
        kingEllipse.setFill(type == PieceColor.BLACK ? Color.BROWN : Color.WHITE);

        kingEllipse.setStroke(Color.BLACK);
        kingEllipse.setStrokeWidth(TILE_SIZE * 0.03);
        kingEllipse.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2 );
        kingEllipse.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2 );



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
        if (this.type == PieceColor.BLACK && y == HEIGHT - 1){
            this.setKing(true);
        }

        if (this.type == PieceColor.WHITE && y == 0){
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
        oldX = x * TILE_SIZE;
        oldY = y * TILE_SIZE;
        relocate(oldX, oldY);
    }

    public void abortMove(){
        relocate(oldX, oldY);
    }



}
