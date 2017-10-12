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


    private DataTuple[] trainingSet;
    private DataTuple[] testSet;


    private ArrayList<Attribute> attributes; //attribut listan ska hålla koll på vilka attribut vi tagit för att inte göra duplicates!

    public DecisionTree buildDecisionTree() {
        splitDataSet();
        initializeAttributesList();
        Node tree = generateTree();
        return null;
    }

    private Node generateTree() {
        Node node;

        if (allTuplesSameClass()) {
            node = new Node(trainingSet[0].DirectionChosen);
        } else if (attributesListEmpty()) {
            node = new Node(getMajorityClass());
        } else {
            node = computeAttributeNode();
        }
        return node;
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

        Attribute attribute = attributeSelection();

        return null;
    }

    //Removes the "best" attribute from the attributes list and returns it.
    //Should be built with id3.
    private Attribute attributeSelection() {
        //TODO Implement
        return null;
    }
    private MOVE getMajorityClass() {

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

    private boolean allTuplesSameClass() {
        MOVE move = trainingSet[0].DirectionChosen;
        for (int i = 1; i < trainingSet.length; i++) {
            if (trainingSet[i].DirectionChosen != move) {
                return false;
            }
        }
        return true;
    }

    private boolean attributesListEmpty() {
        return attributes.isEmpty();
    }

    //Intialize the attribute list with all the attributes.
    private void initializeAttributesList() {
        attributes = new ArrayList<>();
        //TODO Just some random attributes for now, maybe select other attributes.
        attributes.add(Attribute.currentScore);
        attributes.add(Attribute.mazeIndex);
        attributes.add(Attribute.currentLevelTime);
        attributes.add(Attribute.DirectionChosen);
    }

    //Split data set into training and test set.
    private void splitDataSet() {
        DataTuple[] dataSet = DataSaverLoader.LoadPacManData();
        int sixtyPercent = (int) (dataSet.length * 0.6);
        int fortyPercent = (int) (dataSet.length * 0.4);

        trainingSet = new DataTuple[sixtyPercent];
        testSet = new DataTuple[fortyPercent + 1];

        int testIndex = 0;
        for (int i = 0; i <= dataSet.length - 1; i++) {

            if (i < sixtyPercent) {
                trainingSet[i] = dataSet[i];
            } else {
                testSet[testIndex] = dataSet[i];
                testIndex++;
            }
        }
    }
}
