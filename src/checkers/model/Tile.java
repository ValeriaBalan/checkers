package checkers.model;


import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import static checkers.uiGame.UIGame.TILE_SIZE;


public class Tile extends Rectangle {
    private UIPiece UIPiece;

    public boolean hasPiece(){
        return UIPiece != null;
    }


    public UIPiece getUIPiece() {
        return UIPiece;
    }

    public void setUIPiece(UIPiece UIPiece) {
        this.UIPiece = UIPiece;
    }

    public Tile(boolean light, int x, int y){
        setWidth(TILE_SIZE);
        setHeight(TILE_SIZE);
        relocate (x * TILE_SIZE, y * TILE_SIZE);
        setFill(light ? Color.valueOf("#feb") : Color.valueOf("#582") );
    }


    @Override
    public int hashCode() {
        return UIPiece != null ? UIPiece.hashCode() : 0;
    }
}
