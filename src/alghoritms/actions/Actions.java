package alghoritms.actions;

import alghoritms.model.PieceColor;
import alghoritms.model.agent.Piece;
import alghoritms.model.environment.Table;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static alghoritms.actions.Action.*;


public class Actions {

    public static Table newGame(){
        Table table = new Table();
        table = addPiecesForNewGame(table, PieceColor.WHITE, 0);
        table = addPiecesForNewGame(table, PieceColor.BLACK, 5);
        return table;
    }

    public static Table addPiecesForNewGame(Table table, PieceColor pieceColor, int startVerticalPosition){
        for (int i = startVerticalPosition; i < startVerticalPosition + 3; i++){
            for( int j = 0; j < 8; j++){
                if (table.getTable()[i][j].getPieceColor() == PieceColor.BLACK){
                    Piece piece = new Piece(i,j, pieceColor);
                    table.getTable()[i][j].setPiece(piece);
                }
            }
        }
        return table;
    }

    public static void removeFromGame(Piece piece){
        piece.setAlive(false);
        piece.setHorizontalPosition(-1);
        piece.setVerticalPosition(-1);
    }

    public static Boolean onTable(int horizontalPosition, int verticalPosition){
        return horizontalPosition < 8 && horizontalPosition >= 0 && verticalPosition < 8 && verticalPosition >= 0;
    }

    public static Boolean isEmpty(Table table, int verticalPosition, int horizontalPosition){
        if (!onTable(verticalPosition, horizontalPosition)) return false;
        return table.getTable()[verticalPosition][horizontalPosition].getPiece() == null;
    }

    public static PieceColor colorOfThePiece(Table table, int horizontalPosition, int verticalPosition){
        if (!onTable(horizontalPosition, verticalPosition)) return null;
        if (table.getTable()[horizontalPosition][verticalPosition].getPiece() == null) return null;
        return table.getTable()[horizontalPosition][verticalPosition].getPiece().getPieceColor();
    }

    public Piece getPiece(Table table, int horizontalPosition, int verticalPosition){
        if (!onTable(horizontalPosition, verticalPosition)) return null;
        return table.getTable()[horizontalPosition][verticalPosition].getPiece();
    }



    /**
     * @param piece the piece to be set
     * @param horizontalPosition
     * @param verticalPosition
     * @return false if the position is out of table or if there is another piece at that position
     * and true if the piece was set at the required position
     */
    public static boolean setPieceToNewPosition(Table table, Piece piece, int verticalPosition, int horizontalPosition){
        if (!onTable(verticalPosition, horizontalPosition) || !isEmpty(table, verticalPosition, horizontalPosition)){
            return false;
        }
        piece.setHorizontalPosition(horizontalPosition);
        piece.setVerticalPosition(verticalPosition);
        if (!piece.isQueen()){
            piece.setQueen(queenPosition(verticalPosition, piece.getPieceColor()));
        }
        table.getTable()[verticalPosition][horizontalPosition].setPiece(piece);
        return true;
    }

    private static boolean queenPosition(int verticalPosition, PieceColor pieceColor){
        return ((pieceColor == PieceColor.WHITE && verticalPosition == 7) || (pieceColor == PieceColor.BLACK && verticalPosition == 0));
    }


    /**
     * @param horizontalPosition
     * @param verticalPosition
     * @return false if the position is out of table or if there is another piece at that position
     * and true if the piece was deleted
     */
    public static boolean deletePieceFromPosition(Table table, int verticalPosition, int horizontalPosition){
        if (!onTable(verticalPosition, horizontalPosition) || isEmpty(table, verticalPosition, horizontalPosition)){
            return false;
        }
        table.getTable()[verticalPosition][horizontalPosition].setPiece(null);
        return true;
    }

    public static boolean possibleAction(Table table, Piece piece, Action action){
        if (piece == null || !piece.isAlive() || !goodDirection(piece, action)) return false;
        int nextHorizontalPosition = piece.getHorizontalPosition() + action.getHorizontalMove();
        int nextVerticalPosition = piece.getVerticalPosition() + action.getVerticalMove();
        if (!isEmpty(table, nextVerticalPosition, nextHorizontalPosition)) return false;
        if(action.isAttackAction()){
            return possibleAttackAction(table, piece, action);
        }
        return true;
    }

    public static boolean possibleAttackAction(Table table, Piece piece, Action action){
        int newVerticalPos = piece.getVerticalPosition() + action.getVerticalMove();
        int newHorizPos = piece.getHorizontalPosition()+action.getHorizontalMove();
        if (!onTable(newHorizPos, newVerticalPos)) return false;
        if (!isEmpty(table, newVerticalPos, newHorizPos)) return false;
        if (!goodDirection(piece, action)) return false;
        int opponentPieceHorizPosition = action.getHorizontalMove() > 0 ? piece.getHorizontalPosition() + 1 : piece.getHorizontalPosition() - 1;
        int opponentPieceVerticalPosition = action.getVerticalMove() > 0 ? piece.getVerticalPosition() + 1 : piece.getVerticalPosition() - 1;
        if (!onTable(opponentPieceVerticalPosition,opponentPieceHorizPosition )) return false;
        Piece opponentPiece = table.getTable()[opponentPieceVerticalPosition][opponentPieceHorizPosition].getPiece();
        return opponentPiece != null && opponentPiece.getPieceColor() == piece.getPieceColor().oppositeColor();
    }

    private static boolean goodDirection(Piece piece, Action action){
        if (piece.isQueen()) return true;
        return possibleActions(piece.getPieceColor()).contains(action);
    }


    public static boolean deletePieceFromTable(Table table, int verticalPosition, int horizontalPosition){
        if (!onTable(horizontalPosition, verticalPosition) || isEmpty(table, verticalPosition, horizontalPosition)){
            return false;
        }
        Piece piece = table.getTable()[verticalPosition][horizontalPosition].getPiece();
        table.getTable()[verticalPosition][horizontalPosition].setPiece(null);
        removeFromGame(piece);
        return true;
    }

    public static int numberOfPieces(Table table, PieceColor pieceColor){
        int nrOfPieces = 0;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (table.getTable()[i][j].getPiece() != null && table.getTable()[i][j].getPiece().getPieceColor() == pieceColor){
                    nrOfPieces ++;
                }
            }
        }
        return nrOfPieces;
    }

    public static PieceColor wonGame(Table table){
        int nrOfWhitePieces = numberOfPieces(table, PieceColor.WHITE);
        int nrOfBlackPieces = numberOfPieces(table, PieceColor.BLACK);
        if (nrOfBlackPieces == 0 && nrOfWhitePieces != 0){
            return PieceColor.WHITE;
        }
        if (nrOfWhitePieces == 0 && nrOfBlackPieces != 0){
            return PieceColor.BLACK;
        }
        return null;
    }

    public static boolean finishedGame(Table table){
        return wonGame(table) != null;
    }

    public static List<Action> getAttackActions(){
        return Arrays.asList(TAKE_PIECE_FRONT_RIGHT, TAKE_PIECE_FRONT_LEFT, TAKE_PIECE_BACK_RIGHT, TAKE_PIECE_BACK_LEFT);
    }

    public static List<Action> possibleActions(PieceColor pieceColor){
        switch (pieceColor){
            case WHITE:
                return Arrays.asList(FRONT_RIGHT, FRONT_LEFT, TAKE_PIECE_FRONT_RIGHT, TAKE_PIECE_FRONT_LEFT);
            case BLACK:
                return Arrays.asList(BACK_RIGHT, BACK_LEFT, TAKE_PIECE_BACK_RIGHT, TAKE_PIECE_BACK_LEFT);
        }
        return new ArrayList<>();
    }


    public static Action getActionByPosition(int oldX, int oldY, int newX, int newY){
        int horizontalMove = newY - oldY;
        int verticalMove =  newX - oldX;
        Action[] actions = Action.values();
        for(Action action : actions){
            if (action.getHorizontalMove() == horizontalMove && action.getVerticalMove() == verticalMove) return action;
        }
        return null;
    }

}
