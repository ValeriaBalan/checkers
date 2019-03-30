package checkers.uiGame;

import alghoritms.actions.Actions;
import alghoritms.actions.Move;

import alghoritms.model.PieceColor;
import checkers.model.*;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class UIGame {

    public static final int TILE_SIZE = 80;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();
    private Tile[][] board = new Tile[WIDTH][HEIGHT];
    private static PieceColor turn;
    private UIPiece uiPieceInUse;
    private static Move lastMove;

    public UIPiece getUIPieceInUse() {
        return uiPieceInUse;
    }

    public void setUIPieceInUse(UIPiece uiPieceInUse) {
        this.uiPieceInUse = uiPieceInUse;
    }

    public Tile[][] getBoard() {
        return board;
    }

    public void setBoard(Tile[][] board) {
        this.board = board;
    }

    public static Move getLastMove() {
        return lastMove;
    }

    public  void setLastMove(Move lastMove) {
        UIGame.lastMove = lastMove;
    }

    public Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        root.getChildren().addAll(tileGroup, pieceGroup);

        for (int y = 0; y < HEIGHT; y++ ){
            for (int x = 0; x < WIDTH; x++){
                Tile tile  = new Tile((x+y) % 2 == 0, x, y);
                board[x][y] = tile;
                tileGroup.getChildren().add(tile);

                UIPiece UIPiece = null;
                if (y <= 2 && (x+y)%2 != 0){
                    UIPiece = makePiece(PieceColor.BLACK, x, y);
                }

                if (y >= 5 && (x+y)%2 != 0){
                    UIPiece = makePiece(PieceColor.WHITE, x, y);

                }
                if (UIPiece != null){
                    tile.setUIPiece(UIPiece);
                    pieceGroup.getChildren().add(UIPiece);
                }
            }
        }

        return root;
    }

    public UIPiece makePiece(PieceColor type, int x, int y ){
        UIPiece uiPiece = new UIPiece(type, x, y);
        uiPiece.setOnMouseReleased(e -> {
            int newX = toBoard(uiPiece.getLayoutX());
            int newY = toBoard(uiPiece.getLayoutY());
            makeOneMove(newX, newY, uiPiece);
        });
        return uiPiece;
    }

    public void moveOnTable(int oldX, int oldY, int newX, int newY){
        UIPiece uiPiece = board[oldX][oldY].getUIPiece();

        makeOneMove(newX, newY, uiPiece);
    }

    public void makeOneMove(int newX, int newY, UIPiece uiPiece){
        MoveResult result = tryMove(uiPiece, newX, newY);
        int x0 = toBoard(uiPiece.getOldX());
        int y0 = toBoard(uiPiece.getOldY());
        switch (result.getType()){
            case NONE:
                uiPiece.abortMove();
                break;
            case NORMAL:
                uiPiece.move(newX, newY);
                board[x0][y0].setUIPiece(null);
                board[newX][newY].setUIPiece(uiPiece);
                lastMove = getMove(y0, 7 - x0, newY, 7 - newX, uiPiece);
                changeTurn();
                uiPiece.verifyIfKing(newY);
                break;
            case KILL:
                uiPiece.move(newX, newY);
                board[x0][y0].setUIPiece(null);
                board[newX][newY].setUIPiece(uiPiece);
                UIPiece otherUIPiece = result.getUIPiece();
                board[toBoard(otherUIPiece.getOldX())][toBoard(otherUIPiece.getOldY())].setUIPiece(null);
                pieceGroup.getChildren().remove(otherUIPiece);
                uiPiece.verifyIfKing(newY);
                lastMove = getMove(y0, 7 - x0, newY, 7 - newX, uiPiece);
                if  (killMoveAvailableForOnePiece(newX, newY)){
                    this.uiPieceInUse = board[newX][newY].getUIPiece();
                }
                else{
                    this.uiPieceInUse = null;
                    changeTurn();
                }
                break;
        }

    }

    public Move getMove(int oldX, int oldY, int newX, int newY, UIPiece uiPiece){
        Move move = getLastMove() != null && getLastMove().getPiece().getPieceColor() == uiPiece.getPiece().getPieceColor() ?
                getLastMove() : new Move();
        move.setPiece(uiPiece.getPiece());
        move.getActions().add(Actions.getActionByPosition(oldX, oldY, oldX + (oldX-newX), oldY + (oldY - newY)));
        return move;
    }



    public boolean killMoveAvailableForOnePiece(int x, int y){
        int sign = board[x][y].getUIPiece().getType().moveDir();
        MoveResult res = this.tryMove(board[x][y].getUIPiece(), x + 2, y + sign*2 );
        MoveResult res1 = this.tryMove(board[x][y].getUIPiece(), x - 2, y + sign*2 );
        if (res.getType() == MoveType.KILL ||res1.getType() == MoveType.KILL){
            System.out.println("gasit kill move!!!");
            return true;
        }
        return false;
    }



    public boolean killMoveAvailable(){
        for (int x = 0; x < WIDTH; x++){
            for (int y = 0; y < HEIGHT; y++){
                if (board[x][y].hasPiece() && board[x][y].getUIPiece().getType() == turn){
                    if (killMoveAvailableForOnePiece(x,y)) return true;
                }
            }
        }
        return false;
    }
    public void changeTurn(){
      turn = turn.oppositeColor();
    }



    private MoveResult tryMove(UIPiece uiPiece, int newX, int newY){
        if (this.uiPieceInUse != null && this.uiPieceInUse != uiPiece){
            return  new MoveResult(MoveType.NONE);
        }
        if (newX < 0 || newX >= WIDTH || newY < 0 || newY >= HEIGHT ){
            return  new MoveResult(MoveType.NONE);
        }
        if (uiPiece.getType() != turn){
            return  new MoveResult(MoveType.NONE);
        }

        if (board[newX][newY].hasPiece() || (newX + newY ) % 2 == 0){
            return  new MoveResult(MoveType.NONE);
        }


        int x0 = toBoard(uiPiece.getOldX());
        int y0 = toBoard(uiPiece.getOldY());

        if (!uiPiece.isKing()){

            if (Math.abs(newX - x0) == 1 && newY - y0 == uiPiece.getType().moveDir() &&  !killMoveAvailable()){

                return  new MoveResult(MoveType.NORMAL);
            }
            else if(Math.abs(newX - x0) == 2 && newY - y0 == uiPiece.getType().moveDir() * 2 ){
                int x1 = x0 + (newX - x0) / 2;
                int y1 = y0 + (newY - y0) / 2;

                if (board[x1][y1].hasPiece() && board[x1][y1].getUIPiece().getType() != uiPiece.getType() ){
                    return new MoveResult(MoveType.KILL, board[x1][y1].getUIPiece());
                }

            }

        }else{

            if (Math.abs(newX - x0) == 1 && Math.abs(newY - y0) == 1 &&  !killMoveAvailable()){

                return  new MoveResult(MoveType.NORMAL);
            }
            else if(Math.abs(newX - x0) == 2 && Math.abs(newY - y0) == 2 ){
                int x1 = x0 + (newX - x0) / 2;
                int y1 = y0 + (newY - y0) / 2;

                if (board[x1][y1].hasPiece() && board[x1][y1].getUIPiece().getType() != uiPiece.getType() ){
                    return new MoveResult(MoveType.KILL, board[x1][y1].getUIPiece());
                }

            }

        }


        return new MoveResult(MoveType.NONE);
    }



    private int toBoard(double pixel){
        return (int)(pixel + TILE_SIZE / 2) / TILE_SIZE;
    }

    public static PieceColor getTurn() {
        return turn;
    }

    public void setTurn(PieceColor turn) {
        UIGame.turn = turn;
    }
}
