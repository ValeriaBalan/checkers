package alghoritms.players.minmax;



import alghoritms.actions.Action;
import alghoritms.actions.Actions;
import alghoritms.actions.GameMoves;
import alghoritms.model.PieceColor;
import alghoritms.model.agent.Piece;
import alghoritms.model.environment.Table;

import java.util.ArrayList;
import java.util.List;

public class MinMaxUtil {

    public static int currentStateEvaluation(Table table, PieceColor pieceColor){
        List<Piece> currentPlayerPieces = GameMoves.getPlayerPieces(table, pieceColor);
        List<Piece> opponentPlayerPieces = GameMoves.getPlayerPieces(table, pieceColor.oppositeColor());
        if (currentPlayerPieces.isEmpty()) return Integer.MIN_VALUE;
        if (opponentPlayerPieces.isEmpty()) return Integer.MAX_VALUE;
        return currentPlayerPieces.stream().mapToInt(piece -> onePieceEval(table,piece)).sum()
                - opponentPlayerPieces.stream().mapToInt(piece -> onePieceEval(table,piece)).sum();

    }

    public static int onePieceEval(Table table, Piece piece){
        int onePieceEval = 0;
        onePieceEval += piece.isQueen() ? 20 : 2 * distanceFromMargin(piece.getVerticalPosition(), piece.getPieceColor());
        if (vulnerablePosition(table, piece) && !inAttackPosition(table, piece)) {
            onePieceEval -=20;
        }
        if (inAttackPosition(table,piece)){
            onePieceEval +=20;
        }
        return onePieceEval;
    }

    public static boolean vulnerablePosition(Table table, Piece piece){
        List<Piece> neighbors = getOppositeNeighbors(table,piece);
        boolean vulnerablePosition = false;
        for (Piece neighbor : neighbors){
            if (inFront(piece, neighbor) || neighbor.isQueen()){
                int oppositeHorizontalPosition = piece.getHorizontalPosition() < neighbor.getHorizontalPosition() ?
                        piece.getHorizontalPosition() - 1 : piece.getHorizontalPosition() + 1;
                int oppositeVerticalPosition = piece.getVerticalPosition() < neighbor.getVerticalPosition() ?
                        piece.getVerticalPosition() - 1 : piece.getVerticalPosition() + 1;
                if (!Actions.onTable(oppositeHorizontalPosition, oppositeVerticalPosition)) return false;
                if (table.getTable()[oppositeVerticalPosition][oppositeHorizontalPosition].getPiece() == null){
                    return true;
                }
            }
        }
        return vulnerablePosition;
    }

    public static boolean inAttackPosition(Table table, Piece piece){
        List<Action> attackActions = Actions.getAttackActions();
        for(Action action : attackActions){
            if (Actions.possibleAction(table, piece, action)){
                return true;
            }
        }
        return false;
    }



    private static boolean inFront(Piece piece, Piece neighbor){
        return piece.getPieceColor() == PieceColor.WHITE ? neighbor.getVerticalPosition() > piece.getVerticalPosition()
                : neighbor.getVerticalPosition() < piece.getVerticalPosition();
    }

    private static List<Piece> getOppositeNeighbors(Table table, Piece piece) {
        List<Piece> pieces = new ArrayList<>();
        for (int i = piece.getVerticalPosition() - 1; i < piece.getVerticalPosition() + 1; i++){
            for (int j = piece.getHorizontalPosition() -1; i < piece.getHorizontalPosition() + 1; i++){
                if (Actions.onTable(i, j) && !Actions.isEmpty(table, i,j) && table.getTable()[i][j].getPiece().getPieceColor() == piece.getPieceColor().oppositeColor()){
                    pieces.add(table.getTable()[i][j].getPiece());
                }
            }
        }
        return pieces;
    }

    private static int distanceFromMargin(int verticalPosition, PieceColor pieceColor){
        return pieceColor == PieceColor.WHITE ? verticalPosition : 7 - verticalPosition;
    }


}
