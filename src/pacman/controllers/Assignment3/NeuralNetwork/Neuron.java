package pacman.controllers.Assignment3.NeuralNetwork;

class Neuron {

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
