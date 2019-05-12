package alghoritms.players.minmax;



import alghoritms.actions.GameMoves;
import alghoritms.actions.Move;
import alghoritms.model.PieceColor;
import alghoritms.model.environment.Table;
import alghoritms.players.Player;
import alghoritms.players.utils.AIAlgorithmsUtil;
import alghoritms.players.utils.StateNode;

import java.util.List;
import java.util.Random;

import static alghoritms.players.utils.AIAlgorithmsUtil.getAllNodesForCurrentState;
import static alghoritms.players.utils.AIAlgorithmsUtil.getRandomMove;

public class MinMaxAlgorithmPlayer extends Player {

    public MinMaxAlgorithmPlayer(PieceColor pieceColor) {
        super(pieceColor);
    }

    @Override
    public Move getNextMove(Table currentState){
        Random random = new Random();
        List<Move> possibleMoves = GameMoves.getPossibleGameMovesForOnePlayer(currentState, getPieceColor());
        if(random.nextDouble() < 0.1 && possibleMoves.size() > 1) {
            return getRandomMove(possibleMoves);
        }
        StateNode rootNode = getAllNodesForCurrentState(getPieceColor(), currentState);
        rootNode = addValuesForNodes(getPieceColor(), rootNode);
        return rootNode.getMove();
    }

    @Override
    public String getPlayerType() {
        return "Min Max Player";
    }


    public StateNode addValuesForNodes(PieceColor player, StateNode rootNode){
        int maxValue = Integer.MIN_VALUE;
        Move move = null;
        for (StateNode firstLevelChild : rootNode.getChildrenNodes()){
            int minValue = Integer.MAX_VALUE;
            for (StateNode secondLevelChild : firstLevelChild.getChildrenNodes()){
                int value = AIAlgorithmsUtil.currentStateEvaluation(secondLevelChild.getState(), player);
                secondLevelChild.setValue(value);
                if (value < minValue){
                    minValue = value;
                }
            }
            firstLevelChild.setValue(minValue);
            if (minValue >= maxValue){
                maxValue = minValue;
                move = firstLevelChild.getMove();
            }
        }
        rootNode.setValue(maxValue);
        rootNode.setMove(move);
        return rootNode;
    }
}
