package alghoritms.players.consolePlayer;



import alghoritms.actions.Action;
import alghoritms.actions.GameMoves;
import alghoritms.actions.Move;
import alghoritms.model.PieceColor;
import alghoritms.model.agent.Piece;
import alghoritms.model.environment.Table;
import alghoritms.players.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class ConsolePlayer extends Player {
    public ConsolePlayer(PieceColor pieceColor) {
        super(pieceColor);
    }

    @Override
    public Move getNextMove(Table currentState) {
        Move move = new Move();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Piece piece = null;
        System.out.println("Insert next move:");
        boolean existentPiece = false;
        while(!existentPiece){
            System.out.println("Piece position:");
            try {
                String input = reader.readLine();
                String[] positions = input.split(" ");
                piece = currentState.getTable()[Integer.parseInt(positions[0])][Integer.parseInt(positions[1])].getPiece();
                if (piece != null && piece.getPieceColor() == getPieceColor() && !GameMoves.movesForOnePiece(currentState, piece).isEmpty()){
                    existentPiece = true;
                }else {
                    System.out.println("Invalid piece!!!");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
                System.out.println("Invalid position");
            }
        }
//        move.setPiece(piece);

        boolean validAction = false;
        Action[] actions = Action.values();
        while (!validAction){
            System.out.println("Choose action:");
            for(Action action:actions){
                System.out.println(action.stringValue());
            }
            try {
                String action = reader.readLine();
                Integer intValue = Integer.parseInt(action);
                if (0 <= intValue && intValue < 8 && findMove(currentState, piece, intValue) != null){
                    validAction = true;
                    move = findMove(currentState, piece, intValue);
//                    Action nextAction = Action.getActionByIntValue(intValue);
//                    if (nextAction.isAttackAction()){
//                        List<Action> attackActions = GameMoves.chainedAttackActions(currentState, piece, nextAction);
//                        move.setActions(attackActions);
//                    }else {
//                        move.getActions().add(nextAction);
//                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (Exception e){
                System.out.println("Invalid action value");
            }

        }

        return move;
    }

    private Move findMove(Table currentState, Piece piece, int actionIntValue){
        Action action = Action.getActionByIntValue(actionIntValue);
        List<Move> movesForOnePiece = GameMoves.movesForOnePiece(currentState, piece);
        if (movesForOnePiece.stream().anyMatch(Move::getAttack)) {
            movesForOnePiece = movesForOnePiece.stream().filter(Move::getAttack).collect(Collectors.toList());
        }
        return movesForOnePiece.stream().filter(move -> move.getActions().get(0).compareTo(action) == 0).findFirst().orElse(null);
    }
}
