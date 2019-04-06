package uicheckers.model;


import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import static uicheckers.uiGame.UIGame.TILE_SIZE;


public class Tile extends Rectangle {
    private UIPiece uiPiece;

    public boolean hasPiece(){
        return uiPiece != null;
    }


    public UIPiece getUIPiece() {
        return uiPiece;
    }

    public void setUIPiece(UIPiece uiPiece) {
        this.uiPiece = uiPiece;
    }

    public Tile(boolean light, int x, int y){
        setWidth(TILE_SIZE);
        setHeight(TILE_SIZE);
        relocate (x * TILE_SIZE, y * TILE_SIZE);
        setFill(light ? Color.valueOf("#feb") : Color.valueOf("#582") );
    }


    @Override
    public int hashCode() {
        return uiPiece != null ? uiPiece.hashCode() : 0;
    }
}
