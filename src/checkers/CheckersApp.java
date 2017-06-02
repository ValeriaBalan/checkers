package checkers;

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
    private PieceType turn;

    private Piece pieceInUse;




    @Override
    public void start(Stage primaryStage) throws Exception {

        Scene scene = new Scene(createContent());
        primaryStage.setTitle("CheckersApp");
        primaryStage.setScene(scene);
        primaryStage.show();
        this.setTurn(PieceType.WHITE);


    }




    public Piece getPieceInUse() {
        return pieceInUse;
    }

    public void setPieceInUse(Piece pieceInUse) {
        this.pieceInUse = pieceInUse;
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
        piece.setOnMouseReleased(e -> {
            int newX = toBoard(piece.getLayoutX());
            int newY = toBoard(piece.getLayoutY());
            MoveResult result = tryMove(piece, newX, newY);
            int x0 = toBoard(piece.getOldX());
            int y0 = toBoard(piece.getOldY());

            switch (result.getType()){
                case NONE:
                    piece.abortMove();
                    break;
                case NORMAL:
                    piece.move(newX, newY);
                    board[x0][y0].setPiece(null);
                    board[newX][newY].setPiece(piece);
                    changeTurn();
                    piece.verifyIfKing(newY);
                    break;
                case KILL:
                    piece.move(newX, newY);
                    board[x0][y0].setPiece(null);
                    board[newX][newY].setPiece(piece);
                    Piece otherPiece = result.getPiece();
                    board[toBoard(otherPiece.getOldX())][toBoard(otherPiece.getOldY())].setPiece(null);
                    pieceGroup.getChildren().remove(otherPiece);
                    piece.verifyIfKing(newY);
                    if  (killMoveAvailableForOnePiece(newX, newY)){
                        this.pieceInUse = board[newX][newY].getPiece();
                    }
                    else{
                        this.pieceInUse = null;
                        changeTurn();
                    }
                    break;
            }
        });
        return piece;
    }



    public boolean killMoveAvailableForOnePiece(int x, int y){
        int sign = board[x][y].getPiece().getType().moveDir;
        MoveResult res = this.tryMove(board[x][y].getPiece(), x+2, y + sign*2 );
        MoveResult res1 = this.tryMove(board[x][y].getPiece(), x-2, y + sign*2 );
        if (res.getType() == MoveType.KILL ||res1.getType() == MoveType.KILL){
            System.out.println("gasit kill move!!!");
            return true;
        }
        return false;
    }



    public boolean killMoveAvailable(){
        for (int x = 0; x < WIDTH; x++){
            for (int y = 0; y < HEIGHT; y++){
                if (board[x][y].hasPiece() && board[x][y].getPiece().getType() == turn){
                    if (killMoveAvailableForOnePiece(x,y)) return true;
                }
            }
        }
        return false;
    }
    public void changeTurn(){
        if (turn == PieceType.BLACK){
            turn = PieceType.WHITE;
        }
        else{
            turn = PieceType.BLACK;
        }
    }

    public static void main(String[] args){
        launch(args);
    }

    private MoveResult tryMove(Piece piece, int newX, int newY){
        if (this.pieceInUse != null && this.pieceInUse != piece){
            return  new MoveResult(MoveType.NONE);
        }
        if (newX<0 || newX >= WIDTH || newY < 0 || newY >= HEIGHT ){
            return  new MoveResult(MoveType.NONE);
        }
        if (piece.getType() != turn){
            return  new MoveResult(MoveType.NONE);
        }

        if (board[newX][newY].hasPiece() || (newX + newY ) % 2 == 0){
            return  new MoveResult(MoveType.NONE);
        }


        int x0 = toBoard(piece.getOldX());
        int y0 = toBoard(piece.getOldY());

        if (!piece.isKing()){

            if (Math.abs(newX - x0) == 1 && newY - y0 == piece.getType().moveDir &&  !killMoveAvailable()){

                return  new MoveResult(MoveType.NORMAL);
            }
            else if(Math.abs(newX - x0) == 2 && newY - y0 == piece.getType().moveDir * 2 ){
                int x1 = x0 + (newX - x0) / 2;
                int y1 = y0 + (newY - y0) / 2;

                if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType() ){
                    return new MoveResult(MoveType.KILL, board[x1][y1].getPiece());
                }

            }

        }else{

            if (Math.abs(newX - x0) == 1 && Math.abs(newY - y0) == 1 &&  !killMoveAvailable()){

                return  new MoveResult(MoveType.NORMAL);
            }
            else if(Math.abs(newX - x0) == 2 && Math.abs(newY - y0) == 2 ){
                int x1 = x0 + (newX - x0) / 2;
                int y1 = y0 + (newY - y0) / 2;

                if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType() ){
                    return new MoveResult(MoveType.KILL, board[x1][y1].getPiece());
                }

            }

        }


        return new MoveResult(MoveType.NONE);
    }



    private int toBoard(double pixel){
        return (int)(pixel + TILE_SIZE / 2) / TILE_SIZE;
    }

    public PieceType getTurn() {
        return turn;
    }

    public void setTurn(PieceType turn) {
        this.turn = turn;
    }
}
