package pacman.controllers.Assignment3.NeuralNetwork;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Neuron {

    private double value;
    private double[] synapses;

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setSynapseWeights(double[] synapses) {
        this.synapses = synapses;
    }
    public double getSpecificSynapseWeight(int index){
        return synapses[index];
    }
}
