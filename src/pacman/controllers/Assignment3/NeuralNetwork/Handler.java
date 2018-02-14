package pacman.controllers.Assignment3.NeuralNetwork;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;

//Highly experimental for now.
public class Handler {
    //Number of neurons are bound to change.
    private Neuron[] inputLayer = new Neuron[2]; //a number of input neurons.
    private Neuron[] hiddenLayer1 = new Neuron[3];//The hidden layer.
    private Neuron[] outputLayer = new Neuron[5]; //five different directions.

    public Handler() {
        //Create the layers:

        for (int i = 0; i < inputLayer.length; i++) {
            inputLayer[i] = new Neuron();
        }

        for (int i = 0; i < hiddenLayer1.length; i++) {
            hiddenLayer1[i] = new Neuron();
        }

        //TODO: Output neurons should be pacman directions.
        for (int i = 0; i < outputLayer.length; i++) {
            outputLayer[i] = new Neuron();
        }
        startLearning();
    }

    public void startLearning() {
        DataTuple[] dataset = getTrainingSet();

        //The two input nodes values, shouldn't change.
        inputLayer[0].setValue(dataset[0].normalizePosition(dataset[0].pacmanPosition));
        inputLayer[1].setValue(dataset[1].normalizeNumberOfPills(dataset[1].numOfPillsLeft));

        //Set the weights of the input nodes synaps. Just random numbers.
        inputLayer[0].setSynapseWeights(new double[]{0.9, 0.2, 0.6});
        inputLayer[1].setSynapseWeights(new double[]{0.2, 0.7, 0.2});

        //Set the weights of the hidden nodes synaps. Just random numbers.
        hiddenLayer1[0].setSynapseWeights(new double[]{0.1, 0.2, 0.3, 0.1, 0.1});
        hiddenLayer1[1].setSynapseWeights(new double[]{0.4, 0.7, 0.3, 0.2, 0.9});
        hiddenLayer1[2].setSynapseWeights(new double[]{0.9, 0.1, 0.4, 0.5, 0.5});


        for (int i = 0; i < dataset.length; i++) { //TODO Not sure how long it should loop.

            for (int j = 0; j < hiddenLayer1.length; j++) {
                double hiddenSum = 0;
                for (int k = 0; k < inputLayer.length; k++) {
                    hiddenSum += inputLayer[k].getValue() * inputLayer[k].getSpecificSynapseWeight(j);
                }
                hiddenLayer1[j].setValue(activationFunction(hiddenSum));
            }

            //Now all hidden layers have their value. Continue to output neurons.
            for (int j = 0; j < outputLayer.length; j++) {
                double outputSum = 0;
                for (int k = 0; k < hiddenLayer1.length; k++) {
                    outputSum += hiddenLayer1[k].getValue() * hiddenLayer1[k].getSpecificSynapseWeight(j);
                }
                outputLayer[j].setValue(activationFunction(outputSum));
            }

            //Now we are done, we have five values in the output neurons. 
            for (int j = 0; j < outputLayer.length; j++) {
                System.out.println(outputLayer[j].getValue());
            }
            Neuron n = getMaxValue(outputLayer);

        }
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

    //Sigmoid algo
    private double activationFunction(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    //Returns the 70% of the first tuples.
    private DataTuple[] getTrainingSet() {
        DataTuple[] dataSet = DataSaverLoader.LoadPacManData();
        int seventyPercent = (int) (dataSet.length * 0.7);
        DataTuple[] trainingSet = new DataTuple[seventyPercent];
        System.arraycopy(dataSet, 0, trainingSet, 0, seventyPercent);
        return trainingSet;
    }
}