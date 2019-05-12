package alghoritms.players.neuralNetwokPlayer;

import alghoritms.actions.Action;
import alghoritms.actions.GameMoves;
import alghoritms.actions.Move;
import alghoritms.model.PieceColor;
import alghoritms.model.agent.Piece;
import alghoritms.model.environment.Table;
import alghoritms.players.Player;
import alghoritms.players.utils.StateNode;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static alghoritms.players.utils.AIAlgorithmsUtil.*;

public class NeuralNetworkPlayer extends Player {

    private MultiLayerNetwork neuralNetwork;
    private boolean trainingMode = true;
    private  static double EPSILON = 0.4;
    private static final double GAMA = 0.8;

    public NeuralNetworkPlayer(PieceColor pieceColor) {
        super(pieceColor);
    }

    public void initTheNetwork(){
//        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
//                .weightInit(WeightInit.XAVIER)
//                .activation("relu")
//                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
//                .list()
//                .backprop(true)
//                .build();

        MultiLayerConfiguration configuration =  new NeuralNetConfiguration.Builder()
                .seed(12345)
                .iterations(1)
                .weightInit(WeightInit.XAVIER)
                .updater(Updater.ADAGRAD)
                .activation(Activation.RELU)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .learningRate(0.05)
                .regularization(true).l2(0.0001)
                .list()
                .layer(0, new DenseLayer.Builder().nIn(32).nOut(250).weightInit(WeightInit.XAVIER).activation(Activation.RELU) //First hidden layer
                        .build())
                .layer(1, new OutputLayer.Builder().nIn(250).nOut(400).weightInit(WeightInit.XAVIER).activation(Activation.RELU) //Second hidden layer
                        .build())
                .layer(2, new OutputLayer.Builder().nIn(400).nOut(300).weightInit(WeightInit.XAVIER).activation(Activation.RELU) //Third hidden layer
                        .build())
                .layer(3, new OutputLayer.Builder().nIn(300).nOut(256).weightInit(WeightInit.XAVIER).activation(Activation.SOFTMAX) //Output layer
                        .lossFunction(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .build())
                .pretrain(false).backprop(true)
                .build();
        neuralNetwork = new MultiLayerNetwork(configuration);
        neuralNetwork.init();
    }

    @Override
    public Move getNextMove(Table currentState) {
        Random random = new Random();
        Move selectedMove = null;
        while (selectedMove == null){
            int targetNeuron = -1;
            if (random.nextDouble() < EPSILON){
                //get Move from network
                 targetNeuron = getNeuronFired(currentState);
                 selectedMove = getMoveFromNeuronFired(currentState, targetNeuron);
            }else{
                List<Move> possibleMoves = GameMoves.getPossibleGameMovesForOnePlayer(currentState, getPieceColor());
                selectedMove = getRandomMove(possibleMoves);
                targetNeuron = getTargetNeuronFromMove(selectedMove);
            }
            double targetQValue = calculateTargetQLearningValue(currentState, this.getPieceColor(), selectedMove);
            trainTheNetwork(currentState, targetQValue, targetNeuron);
        }
        return selectedMove;
    }

    @Override
    public String getPlayerType() {
        return "Neural Network Player";
    }

    private int getTargetNeuronFromMove(Move move){
        Piece piece = move.getPiece();
        int pieceIntValue = piece.getVerticalPosition() * 4 + piece.getVerticalPosition() % 2 == 0 ? piece.getHorizontalPosition() : piece.getHorizontalPosition() - 1;
        Action action = move.getActions().get(0);
        return pieceIntValue * 8 + action.toInt();
    }


    private double getMaxValueFromNetwork(Table futureState){
        Number max =  neuralNetwork.output(getInputValues(futureState)).maxNumber();
        return max.doubleValue();
    }

    private int getNeuronFired(Table currentState){
        return neuralNetwork.output(getInputValues(currentState)).argMax(0).getInt(0);
    }

    private void trainTheNetwork(Table currentState, double targetValue, int targetNeuron){
        INDArray inputArray = getInputValues(currentState);
        INDArray output = neuralNetwork.getLabels();
        output.putScalar(targetNeuron, targetValue);
        neuralNetwork.setLabels(output);
        neuralNetwork.setInput(inputArray);
        neuralNetwork.fit();
    }

    private INDArray getInputValues(Table currentState){
        INDArray input = Nd4j.zeros(32,3);
        int row = 0;
        for (int i = 0; i < 8; i ++){
            for (int j = 0; j < 8; j++){
                if (currentState.getTable()[i][j].getPieceColor() == PieceColor.BLACK){
                    Piece piece = currentState.getTable()[i][j].getPiece();
                    if ( piece != null){
                        switch (piece.getPieceColor()){
                            case WHITE:
                                input.putScalar(new int[]{row, 0}, 1);
                                break;
                            case BLACK:
                                input.putScalar(new int[]{row, 2}, 1);
                                break;
                        }
                        if (piece.isQueen()){
                            input.putScalar(new int[]{row, 1}, 1);
                        }
                    }
                    row ++;
                }
            }
        }
        return input;
    }

    private double calculateTargetQLearningValue(Table currentState, PieceColor color, Move selectedMove){
        //𝑄(𝑠,𝑎) = 𝑅(𝑠,𝑎,𝑠′) + 𝛾 * max𝑎𝑄(𝑠′,𝑎′)
        Table nextState = selectedMove != null ? GameMoves.makeOneMove(currentState, selectedMove) : null;
        int currentReword = nextState != null ? currentStateEvaluation(nextState, color) : -10000;
        return currentReword + GAMA * getMaxQValueForNextStates(color, currentState);
    }

    private Move getMoveFromNeuronFired(Table table, int neuronFired){
        int actionInt = neuronFired % 8;
        int pieceNumber = neuronFired / 8;
        Action action = Action.getActionByIntValue(actionInt);
        Piece piece = table.getTable()[getVerticalValue(pieceNumber)][getHorizontalValue(pieceNumber)].getPiece();
        if (piece == null) return null;
        return possibleMoveForPiece(table, piece, action);

    }

    private int getVerticalValue(int pieceNumber){
        return pieceNumber / 4;
    }

    private int getHorizontalValue(int pieceNumber){
        int horizontalValue = pieceNumber % 4;
        return getVerticalValue(pieceNumber) % 2 == 0 ? horizontalValue : horizontalValue + 1;
    }

    private double getMaxQValueForNextStates(PieceColor color, Table currentState){
        StateNode currentStateNode = getAllNodesForCurrentState(color, currentState);
        double maxQValue = Double.MIN_VALUE;
        for (StateNode opponentStates : currentStateNode.getChildrenNodes()){
            for (StateNode futureState : opponentStates.getChildrenNodes()){
                double futureStateQValue = getMaxValueFromNetwork(futureState.getState());
                if ( futureStateQValue > maxQValue){
                    maxQValue = futureStateQValue;
                }
            }
        }
        return maxQValue;
    }

    public void saveModel(String filePath){
        File file = new File(filePath);
        try {
            ModelSerializer.writeModel(this.neuralNetwork, file, false);
        } catch (IOException e) {
            System.out.println("Writing the model failed...");
            e.printStackTrace();
        }
    }

    public void setTrainingMode(boolean trainingMode) {
        this.trainingMode = trainingMode;
    }
}
