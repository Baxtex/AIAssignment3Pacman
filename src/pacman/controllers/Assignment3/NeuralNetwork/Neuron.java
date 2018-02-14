package pacman.controllers.Assignment3.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;

public class Neuron {

    private double value;
        private List<Double> synapses = new ArrayList<Double>();

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void addSynapseWeight(double weight) {
        synapses.add(weight);
    }
}
