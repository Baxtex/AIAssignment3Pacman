package pacman.controllers.Assignment3.NeuralNetwork;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;

import java.util.Arrays;

//Highly experimental for now. Contains a lot of weird code that will be refactored.
public class NeuralNetwork {

    //Number of neurons are bound to change.
    private final Neuron[] inputLayer = new Neuron[2];
    private final Neuron[] hiddenLayer = new Neuron[3];
    private final OutputNeuron[] outputLayer = new OutputNeuron[5];
    private final double[] targetValues = new double[]{0.0, 0.2, 0.4, 0.6, 0.8};//NEUTRAL, UP, RIGHT, DOWN, LEFT

    private final DataTuple[] dataset = getTrainingSet();

    public NeuralNetwork() {
        startLearning();
    }

    public void startLearning() {
        initialNN();
        for (int i = 0; i < 5; i++) { //Not sure how long yet.
            backwardPropagation();
            forwardPropagation();
        }
    }

    //Maybe converted to run forward propagation.
    private void initialNN() {
        //Create the layers:
        Arrays.fill(inputLayer, new Neuron());
        Arrays.fill(hiddenLayer, new Neuron());
        for (int i = 0; i < outputLayer.length; i++) {
            outputLayer[i] = new OutputNeuron(targetValues[i]);
        }

        //The two input nodes values
        inputLayer[0].setValue(dataset[0].normalizePosition(dataset[0].pacmanPosition));
        inputLayer[1].setValue(dataset[0].normalizeNumberOfPills(dataset[0].numOfPillsLeft));
        //Set the weights of the input nodes synapse. Just random numbers.
        inputLayer[0].setSynapseWeights(new double[]{0.9, 0.2, 0.6});
        inputLayer[1].setSynapseWeights(new double[]{0.2, 0.7, 0.2});
        //Set the weights of the hidden nodes synapse. Just random numbers.
        hiddenLayer[0].setSynapseWeights(new double[]{0.1, 0.2, 0.3, 0.1, 0.1});
        hiddenLayer[1].setSynapseWeights(new double[]{0.4, 0.7, 0.3, 0.2, 0.9});
        hiddenLayer[2].setSynapseWeights(new double[]{0.9, 0.1, 0.4, 0.5, 0.5});

        for (int j = 0; j < hiddenLayer.length; j++) {
            double hiddenSum = 0;
            for (Neuron anInputLayer : inputLayer) {
                hiddenSum += anInputLayer.getValue() * anInputLayer.getSpecificSynapseWeight(j);
            }
            hiddenLayer[j].setValue(activationFunction(hiddenSum));
        }

        //Now all hidden layers have their value. Continue to output neurons.
        for (int j = 0; j < outputLayer.length; j++) {
            double outputSum = 0;
            for (Neuron neuron : hiddenLayer) {
                outputSum += neuron.getValue() * neuron.getSpecificSynapseWeight(j);
            }
            outputLayer[j].setValue(outputSum);
            outputLayer[j].setValueAfterActivation(activationFunction(outputSum));
        }

        //Now we are done, we have five values in the output neurons.
        for (OutputNeuron neuron : outputLayer) {
            System.out.println(neuron.getValue());
        }
        Neuron n = getMaxValue(outputLayer);
    }

    //Should go back and tweak the weights.
    private void backwardPropagation() {
        double[][] newWeights = new double[hiddenLayer.length][hiddenLayer.length];
        //First part, calcs weights from hidden to outputLayer:
        for (int i = 0; i < hiddenLayer.length; i++) {
            double[] newOutputWeights = new double[hiddenLayer.length];
            for (int j = 0; j < outputLayer.length; j++) {
                double outputSumMarginError = (targetValues[j] - outputLayer[j].getValueAfterActivation());
                double deltaOutputSum = sigmoidPrime(outputLayer[j].getValue()) * outputSumMarginError;
                double newWeight = (outputLayer[i].getSpecificSynapseWeight(j)) - (deltaOutputSum / hiddenLayer[i].getValue());
                newOutputWeights[j] = newWeight;
            }
            newWeights[i] = newOutputWeights;
        }

        //TODO: Second part, calcs weights from input to hiddenLayer.
        double[][] newInputWeightsInput = new double[inputLayer.length][hiddenLayer.length];
        for (Neuron neuron : inputLayer) {
            //Need to do more research to understand  what deltaOutputSum I should use.
        }

        //Sets previous weights from Hidden to output. This is because weights from input to hidden needs original weights.
        for (int j = 0; j < hiddenLayer.length; j++) {
            hiddenLayer[j].setSynapseWeights(newWeights[j]);
        }
    }

    //Should go forward and see results with the new weights.
    private void forwardPropagation() {

    }

    //Todo currently doesn't handle if we get duplicates.
    private Neuron getMaxValue(Neuron[] outputLayer) {

        double maxVal = -1;
        int index = -1;

        for (int i = 0; i < outputLayer.length; i++) {
            if (outputLayer[i].getValue() > maxVal) {
                maxVal = outputLayer[i].getValue();
                index = i;
            }
        }
        return outputLayer[index];
    }

    //Sigmoid algorithm
    private double activationFunction(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    private double sigmoidPrime(double x) {
        return activationFunction(x) * (1 - activationFunction(x));
    }

    //Returns the 70% of the first tuples. Will do for now.
    private DataTuple[] getTrainingSet() {
        DataTuple[] dataSet = DataSaverLoader.LoadPacManData();
        int seventyPercent = (int) (dataSet.length * 0.7);
        DataTuple[] trainingSet = new DataTuple[seventyPercent];
        System.arraycopy(dataSet, 0, trainingSet, 0, seventyPercent);
        return trainingSet;
    }
}