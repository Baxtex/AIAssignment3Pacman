package pacman.controllers.Assignment3.NeuralNetwork;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;

import java.util.ArrayList;
import java.util.List;

//Highly experimental for now.
public class Handler {
    //Number of neurons are bound to change.
    private List<Neuron> inputLayer = new ArrayList<>(2);
    private List<Neuron> hiddenLayer1 = new ArrayList<>(3);
    private List<Neuron> outputLayer = new ArrayList<>(5);

    public Handler() {
        //Create the layers:
        for (int i = 0; i < inputLayer.size(); i++) {
            inputLayer.add(new Neuron());
        }

        for (int i = 0; i < hiddenLayer1.size(); i++) {
            inputLayer.add(new Neuron());
        }

        for (int i = 0; i < outputLayer.size(); i++) {
            inputLayer.add(new Neuron());
        }
        startLearning();
    }

    private void startLearning() {
        DataTuple[] dataset = getTrainingSet();

        for (int i = 0; i < dataset.length; i++) {
            //Set the input. Need to convert discrete data first. 
        }
    }
    private double  convertDiscreteToContinuousData(){
        //should convert discrete values into ContinuousData in form of 0..1.
        return 0.0;
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