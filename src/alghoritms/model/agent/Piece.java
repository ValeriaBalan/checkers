package alghoritms.model.agent;


import alghoritms.model.PieceColor;

public class Piece {
    private int verticalPosition;
    private int horizontalPosition;
    private final PieceColor pieceColor;
    boolean alive;
    private boolean queen;

    public Piece(int verticalPosition, int horizontalPosition, PieceColor pieceColor) {
        this.verticalPosition = verticalPosition;
        this.horizontalPosition = horizontalPosition;
        this.pieceColor = pieceColor;
        this.alive = true;
        this.queen = false;
    }

    public Piece (Piece piece){
        this.verticalPosition = piece.getVerticalPosition();
        this.horizontalPosition = piece.getHorizontalPosition();
        this.pieceColor = piece.getPieceColor();
        this.alive = piece.isAlive();
        this.queen = piece.isQueen();
    }

    public int getVerticalPosition() {
        return verticalPosition;
    }

    public void setVerticalPosition(int verticalPosition) {
        this.verticalPosition = verticalPosition;
    }

    public PieceColor getPieceColor() {
        return pieceColor;
    }


    public int getHorizontalPosition() {
        return horizontalPosition;
    }

    public void setHorizontalPosition(int horizontalPosition) {
        this.horizontalPosition = horizontalPosition;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isQueen() {
        return queen;
    }

    public void setQueen(boolean queen) {
        this.queen = queen;
    }

}
