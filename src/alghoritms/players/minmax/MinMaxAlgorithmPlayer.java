package alghoritms.players.minmax;



import alghoritms.actions.GameMoves;
import alghoritms.actions.Move;
import alghoritms.model.PieceColor;
import alghoritms.model.environment.Table;
import alghoritms.players.Player;

import java.util.List;
import java.util.Random;

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
        MinMaxNode rootNode = getAllNodesForCurrentState(getPieceColor(), currentState);
        rootNode = addValuesForNodes(getPieceColor(), rootNode);
        return rootNode.getMove();
    }

    private Move getRandomMove(List<Move> possibleMoves) {
        Random random = new Random();
        int position = random.nextInt(possibleMoves.size() -1);
        return possibleMoves.get(position);
    }

    public MinMaxNode getAllNodesForCurrentState(PieceColor playerPieceColor, Table table){
        List<Move> movesForCurrentPlayer = GameMoves.getPossibleGameMovesForOnePlayer(table, playerPieceColor);
        MinMaxNode rootNode = new MinMaxNode(playerPieceColor, false,table, null);
        for (Move move : movesForCurrentPlayer){
            Table newState = GameMoves.makeOneMove(table, move);
            rootNode.getChildrenNodes().add(new MinMaxNode(playerPieceColor.oppositeColor(), false, newState, move));
        }
        for (MinMaxNode node: rootNode.getChildrenNodes()){
            List<Move> opponentPossibleMoves = GameMoves.getPossibleGameMovesForOnePlayer(node.getState(), node.getPlayerPieceColor());
            for (Move move : opponentPossibleMoves){
                Table newState = GameMoves.makeOneMove(node.getState(), move);
                node.getChildrenNodes().add(new MinMaxNode(playerPieceColor, true, newState, move));
            }
        }

        return rootNode;
    }

    public MinMaxNode addValuesForNodes(PieceColor player, MinMaxNode rootNode){
        int maxValue = Integer.MIN_VALUE;
        Move move = null;
        for (MinMaxNode firstLevelChild : rootNode.getChildrenNodes()){
            int minValue = Integer.MAX_VALUE;
            for (MinMaxNode secondLevelChild : firstLevelChild.getChildrenNodes()){
                int value = MinMaxUtil.currentStateEvaluation(secondLevelChild.getState(), player);
                secondLevelChild.setValue(value);
                if (value < minValue){
                    minValue = value;
                }
            }
            firstLevelChild.setValue(minValue);
            if (minValue > maxValue){
                maxValue = minValue;
                move = firstLevelChild.getMove();
            }
        }
        rootNode.setValue(maxValue);
        rootNode.setMove(move);
        return rootNode;
    }
}
