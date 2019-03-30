package alghoritms.actions;



import alghoritms.model.PieceColor;
import alghoritms.model.agent.Piece;
import alghoritms.model.environment.Table;
import checkers.uiGame.UIGame;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static alghoritms.actions.Actions.setPieceToNewPosition;


public class GameMoves {

    public static Table makeOneMove(Table oldTable, Move move, UIGame...uiGame){
        Table newTable = new Table(oldTable);
        Piece piece = newTable.getTable()[move.getPiece().getVerticalPosition()][move.getPiece().getHorizontalPosition()].getPiece();
        int oldVerticalPosition = piece.getVerticalPosition();
        int oldHorizontalPosition = piece.getHorizontalPosition();
        Actions.deletePieceFromPosition(newTable, piece.getVerticalPosition(), piece.getHorizontalPosition());
        int newHorizontalPosition = piece.getHorizontalPosition();
        int newVerticalPosition = piece.getVerticalPosition();
        for (Action action : move.getActions()){
            if (action.isAttackAction()){
                int opponentHorizontalPosition = action.getHorizontalMove() > 0 ? newHorizontalPosition + 1 : newHorizontalPosition -1;
                int opponentVerticalPosition = action.getVerticalMove() > 0 ? newVerticalPosition + 1 : newVerticalPosition - 1;
                Actions.deletePieceFromTable(newTable, opponentVerticalPosition, opponentHorizontalPosition);
            }
            newHorizontalPosition += action.getHorizontalMove();
            newVerticalPosition +=action.getVerticalMove();
        }
        setPieceToNewPosition(newTable, piece, newVerticalPosition, newHorizontalPosition);
        if (uiGame.length > 0){
            uiGame[0].moveOnTable(oldHorizontalPosition, 7 - oldVerticalPosition, newHorizontalPosition, 7 - newVerticalPosition);
        }
        return newTable;
    }

    public static List<Move> getPossibleGameMovesForOnePlayer(Table table, PieceColor playerPieceColor){
        List<Piece> playerPieces = getPlayerPieces(table, playerPieceColor);
        List<Move> moves = playerPieces.stream().flatMap(piece -> movesForOnePiece(table, piece).stream()).collect(Collectors.toList());
        if (moves.stream().anyMatch(Move::getAttack)){
            moves = moves.stream().filter(Move::getAttack).collect(Collectors.toList());
        }
        return moves;
    }

    public static List<Move> movesForOnePiece(Table table, Piece piece){
       Action[] actions = Action.values();
       List<Move> moves = new ArrayList<>();
       for (Action action : actions){
           if (Actions.possibleAction(table, piece, action)){
               Move move = new Move();
               move.setPiece(piece);
               if (action.isAttackAction()){
                   move.getActions().addAll(chainedAttackActions(table, piece, action));
                   move.setAttack(true);
               }else{
                   move.getActions().add(action);
               }
               moves.add(move);
           }
       }
       return moves;
    }

    public static List<Action> chainedAttackActions(Table table, Piece piece, Action action) {
        List<Action> chainedActions = new ArrayList<>();
        chainedActions.add(action);
        boolean attack = true;
        Piece newPiece = new Piece(piece);
        Action oldAction = action;
        while (attack){
            attack = false;
            List<Action> attackActions = Actions.getAttackActions();
            int newHorizontalPosition = newPiece.getHorizontalPosition() + oldAction.getHorizontalMove();
            int newVerticalPosition = newPiece.getVerticalPosition() + oldAction.getVerticalMove();
            newPiece.setHorizontalPosition(newHorizontalPosition);
            newPiece.setVerticalPosition(newVerticalPosition);
            for (Action attackAction : attackActions){
                if (Actions.possibleAttackAction(table, newPiece, attackAction)){
                    chainedActions.add(attackAction);
                    oldAction = attackAction;
                    attack = true;
                    break;
                }
            }

        }
        return chainedActions;
    }


    public static List<Piece> getPlayerPieces(Table table, PieceColor playerPieceColor){
        List<Piece> playerPieces = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                Piece piece = table.getTable()[i][j].getPiece();
                if ( piece != null && piece.getPieceColor() == playerPieceColor){
                    playerPieces.add(piece);
                }
            }
        }
        return playerPieces;
    }
}
