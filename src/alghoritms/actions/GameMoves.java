package alghoritms.actions;



import alghoritms.model.PieceColor;
import alghoritms.model.agent.Piece;
import alghoritms.model.environment.Table;
import uicheckers.uiGame.UIGame;
import javafx.application.Platform;

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
            oldVerticalPosition = newVerticalPosition;
            oldHorizontalPosition = newHorizontalPosition;
            newHorizontalPosition += action.getHorizontalMove();
            newVerticalPosition +=action.getVerticalMove();
            if (uiGame.length > 0){
                updateUI(uiGame[0], oldHorizontalPosition, oldVerticalPosition, newHorizontalPosition, newVerticalPosition);

            }
        }
        setPieceToNewPosition(newTable, piece, newVerticalPosition, newHorizontalPosition);
        return newTable;
    }

    public static void updateUI(UIGame uiGame, int oldHoriz, int oldVert, int newHoriz, int newVertical){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                uiGame.moveOnTable(oldHoriz, 7 - oldVert, newHoriz, 7 - newVertical);
            }
        });
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
        List<Action> attackActions = Actions.getAttackActions();
        while (attack){
            attack = false;
            int newHorizontalPosition = newPiece.getHorizontalPosition() + oldAction.getHorizontalMove();
            int newVerticalPosition = newPiece.getVerticalPosition() + oldAction.getVerticalMove();
            newPiece.setHorizontalPosition(newHorizontalPosition);
            newPiece.setVerticalPosition(newVerticalPosition);
            for (Action attackAction : attackActions){
                if (attackAction != oldAction.getOpositeAtackAction() && Actions.possibleAttackAction(table, newPiece, attackAction)){
                    chainedActions.add(attackAction);
                    System.out.println("piece oriz: " + newPiece.getHorizontalPosition() + " vertic: " + newPiece.getVerticalPosition() + " new atack action: " + attackAction.stringValue());
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
