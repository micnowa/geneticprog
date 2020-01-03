package neural.genetic.programming;

import neural.genetic.programming.fitness.Operation;
import neural.genetic.programming.logic.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Random;


public class MainClass extends Canvas {
    private static final long serialVersionUID = 1L;

    public MainClass() {
        super();
    }

    private static BufferedImage getScreenShot(Component component) {
        BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
        component.paint(image.getGraphics());
        return image;

    }

    public static void main(String[] args) {
        System.out.println("1-	logic gates");

        int inputNumber = 10, outputNumber = 1, rows = 5, columns = 5;
        double probability = 0.1, recurrentProbability = 0.0;

        System.out.println("Probability:	" + probability);
        Random rand = new Random();

        LinkedList<Operation<Boolean>> operations = logicFunctions();

        Network<Boolean> network = new Network<>(operations, inputNumber, outputNumber, rows, columns, false, probability, recurrentProbability);
        Boolean[] tab = new Boolean[inputNumber];
        for (int i = 0; i < inputNumber; i++) tab[i] = rand.nextBoolean();
        network.setInputValues(tab);
        network.calculatesNeuronsValues();

        // Set of grids
        NetworkGenerator<Boolean> networkGenerator = new NetworkGenerator<>(network);
        networkGenerator.getMainNetwork().calculatesNeuronsValues();
        for (int i = 0; i < NetworkGenerator.getNetworkNumber(); i++) networkGenerator.getNetwork()[i].calculatesNeuronsValues();

        NetworkAlgorithm<Boolean> algorithm = new NetworkAlgorithm<>(networkGenerator, network, new LogicAdderScore());
        Network<Boolean> finalNetwork = algorithm.runAlgorithm();

        finalNetwork.calculatesNeuronsValues();
        System.out.println();
    }

    private static LinkedList<Operation<Boolean>> logicFunctions() {
        LinkedList<Operation<Boolean>> operations = new LinkedList<>();
        operations.add(new And());
        operations.add(new Or());
        operations.add(new Nor());
        operations.add(new Xor());
        operations.add(new Nand());

        return operations;
    }
}

