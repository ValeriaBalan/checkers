package alghoritms.model.environment;


import alghoritms.model.PieceColor;
import alghoritms.model.agent.Piece;

public class TableQuadrant {
    private final PieceColor pieceColor;
    private Piece piece = null;
    private final int verticalPosition;
    private final int horizontalPosition;

    public TableQuadrant(int verticalPosition, int horizontalPosition){
        this.horizontalPosition = horizontalPosition;
        this.verticalPosition = verticalPosition;
        if (verticalPosition % 2 == 0){
            pieceColor = horizontalPosition % 2 == 0 ? PieceColor.BLACK : PieceColor.WHITE;
        }else{
            pieceColor = horizontalPosition % 2 != 0 ? PieceColor.BLACK : PieceColor.WHITE;
        }
    }

    public TableQuadrant(TableQuadrant tableQuadrant){
        this.horizontalPosition = tableQuadrant.getHorizontalPosition();
        this.verticalPosition = tableQuadrant.getVerticalPosition();
        this.pieceColor = tableQuadrant.getPieceColor();
        this.piece = tableQuadrant.getPiece() != null ? new Piece(tableQuadrant.getPiece()) : null;
    }

    public TableQuadrant(int verticalPosition, int horizontalPosition, Piece piece){
        this.horizontalPosition = horizontalPosition;
        this.verticalPosition = verticalPosition;
        if (verticalPosition % 2 == 0){
            pieceColor = horizontalPosition % 2 == 0 ? PieceColor.BLACK : PieceColor.WHITE;
        }else{
            pieceColor = horizontalPosition % 2 != 0 ? PieceColor.BLACK : PieceColor.WHITE;
        }
        this.piece = piece;
    }


    public PieceColor getPieceColor() {
        return pieceColor;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int getVerticalPosition() {
        return verticalPosition;
    }

    public int getHorizontalPosition() {
        return horizontalPosition;
    }
}
