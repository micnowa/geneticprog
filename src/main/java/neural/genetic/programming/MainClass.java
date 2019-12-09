package neural.genetic.programming;

import neural.genetic.programming.fitness.Operation;
import neural.genetic.programming.logic.*;
import neural.genetic.programming.mathematics.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;


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

    public static void saveScreenShot(Component component, String fileName) throws Exception {
        BufferedImage img = getScreenShot(component);
        ImageIO.write(img, "png", new File("/home/ubuntu/Obrazy/" + fileName));
    }


    public static void main(String[] args) {

        System.out.println("Choose logic gate fitter or sinu fitting");
        System.out.println("1-	logic gates");
        System.out.println("2-	sinus");

        int choose;
        Scanner myInput = new Scanner(System.in);
        choose = myInput.nextInt();
        System.out.println("Number is:	" + choose);

        int inputNumber = 4, outputNumber = 1, rows = 10, columns = 10;
        double probability = 0.1, recurrentProbability = 0.0;

        System.out.println("Probability:	" + probability);
        Random rand = new Random();

        // Available Operations
        LinkedList<Operation<Boolean>> operations = logicFunctions();

        // Grid of operations
        Network<Boolean> network = new Network<>(operations, inputNumber, outputNumber, rows, columns, false, probability, recurrentProbability);
        Boolean[] tab = new Boolean[inputNumber];
        for (int ii = 0; ii < inputNumber; ii++) tab[ii] = rand.nextBoolean();
        network.setInputValues(tab);
        network.calculateValueForEveryGate();

        // Set of grids
        NetworkGenerator<Boolean> networkGenerator = new NetworkGenerator<>(network);
        networkGenerator.getMainNetwork().calculateValueForEveryGate();
        for (int i = 0; i < NetworkGenerator.getGridNumber(); i++) {
            networkGenerator.getNetwork()[i].calculateValueForEveryGate();
        }

        FourPlusOneAlgorithm<Boolean> fourPlusOne = new FourPlusOneAlgorithm<>(networkGenerator, network, new LogicGatesFitter());
        Network<Boolean> finalNetwork = fourPlusOne.runAlgorithm();

        finalNetwork.calculateValueForEveryGate();
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

    private static LinkedList<Operation<Double>> sinusFunctions() {
        LinkedList<Operation<Double>> functions = new LinkedList<Operation<Double>>();
        functions.add(new Divide());
        functions.add(new Subtract());
        functions.add(new Multiply());
        functions.add(new Add());
        functions.add(new Power());
        functions.add(new AbsoluteValue());
        functions.add(new Square());
        functions.add(new Cube());
        functions.add(new Sine());
        functions.add(new Cosine());

        return functions;
    }
}

