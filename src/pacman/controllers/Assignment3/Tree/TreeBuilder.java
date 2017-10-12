package pacman.controllers.Assignment3.Tree;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static pacman.game.Constants.MOVE;

/**
 * Should build a decision tree by using the ID3 approach.
 */
public class TreeBuilder {

    public DecisionTree buildDecisionTree() {
        DataTuple[] dataSet = DataSaverLoader.LoadPacManData();
        DataTuple[] trainingSet = getTrainingSet(dataSet);
        DataTuple[] testSet = getTestSet(dataSet, trainingSet.length);
        ArrayList<Attribute> attributes = initializeAttributesList();

        Node tree = generateTree(trainingSet, attributes);

        return null;
    }

    private DataTuple[] getTrainingSet(DataTuple[] dataSet) {
        int sixtyPercent = (int) (dataSet.length * 0.6);
        DataTuple[] trainingSet = new DataTuple[sixtyPercent];
        for (int i = 0; i < sixtyPercent; i++) {
            trainingSet[i] = dataSet[i];
        }
        return trainingSet;
    }

    private DataTuple[] getTestSet(DataTuple[] dataSet, int startIndex) {
        int fortyPercent = (int) (dataSet.length -startIndex);
        DataTuple[] testSet = new DataTuple[fortyPercent];
        for (int i = 0; startIndex<dataSet.length; i++) {
            testSet[i] = dataSet[startIndex];
            startIndex++;
        }
        return testSet;
    }

    //Intialize the attribute list with all the attributes.
    //TODO Just some random attributes for now, maybe select other attributes.
    private ArrayList<Attribute>  initializeAttributesList() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(Attribute.currentScore);
        attributes.add(Attribute.mazeIndex);
        attributes.add(Attribute.currentLevelTime);
        attributes.add(Attribute.DirectionChosen);
        return attributes;
    }

    private Node generateTree(DataTuple[] trainingSet, ArrayList<Attribute> attributes ) {
        Node node;

        if (allTuplesSameClass(trainingSet)) {
            node = new Node(trainingSet[0].DirectionChosen);
        } else if (attributes.isEmpty()) {
            node = new Node(getMajorityClass(trainingSet));
        } else {
            node = computeAttributeNode();
        }
        return node;
    }

    private boolean allTuplesSameClass(DataTuple[] trainingSet) {
        MOVE move = trainingSet[0].DirectionChosen;
        for (int i = 1; i < trainingSet.length; i++) {
            if (trainingSet[i].DirectionChosen != move) {
                return false;
            }
        }
        return true;
    }

    private MOVE getMajorityClass(DataTuple[] trainingSet) {

        class Pair implements Comparable<Pair> {
            MOVE move;
            int numberOfTimes;

            Pair(MOVE move) {
                this.move = move;
            }

            @Override
            public int compareTo(Pair o) {
                return Integer.compare(numberOfTimes, o.numberOfTimes);
            }
        }

        Pair neutral = new Pair(MOVE.NEUTRAL);
        Pair up = new Pair(MOVE.UP);
        Pair right = new Pair(MOVE.RIGHT);
        Pair down = new Pair(MOVE.DOWN);
        Pair left = new Pair(MOVE.LEFT);

        for (int i = 1; i < trainingSet.length; i++) {
            if (trainingSet[i].DirectionChosen == MOVE.NEUTRAL) {
                neutral.numberOfTimes++;
            } else if (trainingSet[i].DirectionChosen == MOVE.UP) {
                up.numberOfTimes++;
            } else if (trainingSet[i].DirectionChosen == MOVE.RIGHT) {
                right.numberOfTimes++;
            } else if (trainingSet[i].DirectionChosen == MOVE.DOWN) {
                down.numberOfTimes++;
            } else if (trainingSet[i].DirectionChosen == MOVE.LEFT) {
                left.numberOfTimes++;
            }
        }

        List<Pair> data = new ArrayList<>();
        data.add(neutral);
        data.add(up);
        data.add(right);
        data.add(down);
        data.add(left);
        Collections.sort(data);
        return data.get(4).move;
    }

    private Node computeAttributeNode() {

        /*
        1. Call the attribute selection method on D and the attribute list, in order to choose the current attribute A:
            S(D, attribute list) -> A.

        2. Label N as A and remove A from the attribute list.

        3. For each value a j in attribute A:
            a) Separate all tuples in D so that attribute A takes the value a j , creating the subset D j .
            b) If D j is empty, add a child node to N labeled with the majority class in D.
            c) Otherwise, add the resulting node from calling Generate_Tree(D j , attribute) as a child node to N.

        4. Return N.
        */

        Attribute attribute = attributeSelection(); // step 1


        return null;
    }

    //Should be built with id3. Removes the "best" attribute from the attributes list and returns it
    //TODO Implement
    private Attribute attributeSelection() {
        return null;
    }
}
