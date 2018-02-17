package pacman.controllers.Assignment3.Controller;

import pacman.controllers.Assignment3.NeuralNetwork.NeuralNetwork;
import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Game;

public class MLNeuralNetwork extends Controller<Constants.MOVE> {

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        neuralNetwork.startLearning();
        return null;
    }
}
