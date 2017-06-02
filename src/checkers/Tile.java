package checkers;


import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;




public class Tile extends Rectangle {
    private Piece piece;

    public boolean hasPiece(){
        return piece != null;
    }


    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Tile(boolean light, int x, int y){
        setWidth(CheckersApp.TILE_SIZE);
        setHeight(CheckersApp.TILE_SIZE);
        relocate (x * CheckersApp.TILE_SIZE, y * CheckersApp.TILE_SIZE);
        setFill(light ? Color.valueOf("#feb") : Color.valueOf("#582") );
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Tile tile = (Tile) o;
//
//        return piece != null ? piece.equals(tile.piece) : tile.piece == null;
//
//    }

    @Override
    public int hashCode() {
        return piece != null ? piece.hashCode() : 0;
    }
}
