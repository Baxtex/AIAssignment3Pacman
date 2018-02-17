package pacman.controllers.Assignment3.NeuralNetwork;

class OutputNeuron extends Neuron {

    private final double target;
    private double valueAfterActivation;

    public OutputNeuron(double target){
        this.target = target;
    }

    public double getValueAfterActivation() {
        return valueAfterActivation;
    }

    public void setValueAfterActivation(double valueAfterActivation) {
        this.valueAfterActivation = valueAfterActivation;
    }
}
