package alghoritms;

import alghoritms.actions.Move;
import alghoritms.model.PieceColor;
import alghoritms.model.environment.Table;
import alghoritms.players.Player;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;



public class NeuralNetworkPlayer extends Player {

    public NeuralNetworkPlayer(PieceColor pieceColor) {
        super(pieceColor);
    }

    @Override
    public Move getNextMove(Table currentState) {
        NeuralNetwork ann = new NeuralNetwork();

        Layer layer = new Layer();
        ann.addLayer(0, layer);
        ann.setInputNeurons(layer.getNeurons());
        return null;
    }
}
