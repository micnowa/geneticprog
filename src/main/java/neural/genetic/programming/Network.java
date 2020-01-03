package neural.genetic.programming;

import neural.genetic.programming.fitness.Operation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Network<T> implements Serializable {
    private int m;
    private int n;
    private int inputNeuronsNumber;
    private Neuron<T>[][] neurons;
    private int inputNumber;
    private Neuron<T>[] input;
    private int outputNumber;
    private Neuron<T>[] output;
    private Random randomGenerator;
    private LinkedList<Operation<T>> operationList;
    private double recurrentProbability;
    private double probability;
    private ArrayList<Neuron<T>> activeNeurons;
    private T initialValue;


    Network(LinkedList<Operation<T>> functions, int inputNum, int outputNum, int m, int n, T initialValue, double probability, double recurrentProbability) {
        this.m = m;
        this.n = n;
        this.neurons = new Neuron[m][n];
        this.randomGenerator = new Random();
        this.recurrentProbability = recurrentProbability;
        this.probability = probability;
        this.initialValue = initialValue;

        this.inputNumber = inputNum;
        this.input = new Neuron[inputNumber];
        for (int i = 0; i < inputNumber; i++) {
            this.input[i] = new Neuron<T>();
            this.input[i].setI(i);
            this.input[i].setJ(-1);
            this.input[i].outputNeurons = new LinkedList<Neuron<T>>();
        }

        this.outputNumber = outputNum;
        this.output = new Neuron[outputNumber];
        for (int i = 0; i < outputNumber; i++) {
            this.output[i] = new Neuron<T>();
            this.output[i].setI(i);
            this.output[i].setJ(n);
            this.output[i].inputNeurons = new LinkedList<Neuron<T>>();
        }

        this.operationList = new LinkedList<Operation<T>>();
        this.inputNeuronsNumber = functions.get(0).argsNumber();
        for (Operation<T> func : functions) {
            this.operationList.add(func);
            if (func.argsNumber() > this.inputNeuronsNumber) this.inputNeuronsNumber = func.argsNumber();
        }
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                this.neurons[i][j] = new Neuron<>(this.operationList.get(this.randomGenerator.nextInt(this.operationList.size())), i, j, initialValue);
            }
        }

        this.activeNeurons = new ArrayList<>();
        connectAllNeurons();
    }


    Network(Network<T> network) {
        m = network.m;
        n = network.n;
        randomGenerator = new Random();
        inputNeuronsNumber = network.getInputNeuronsNumber();
        operationList = new LinkedList<>(network.operationList);
        operationList.addAll(network.operationList);
        inputNumber = network.inputNumber;
        outputNumber = network.outputNumber;
        probability = network.getProbability();
        recurrentProbability = network.getRecurrentProbability();
        initialValue = network.getInitialValue();

        input = new Neuron[inputNumber];
        for (int ii = 0; ii < inputNumber; ii++) {
            input[ii] = new Neuron<T>(null, ii, -1, initialValue);
            input[ii].value = network.input[ii].value;
            input[ii].outputNeurons = new LinkedList<Neuron<T>>();
        }

        output = new Neuron[outputNumber];
        for (int ii = 0; ii < outputNumber; ii++) {
            output[ii] = new Neuron<>(null, ii, n, initialValue);
            output[ii].inputNeurons = new LinkedList<>();
        }

        neurons = new Neuron[m][n];
        for (int ii = 0; ii < m; ii++) {
            for (int jj = 0; jj < n; jj++) {
                neurons[ii][jj] = new Neuron<>(network.neurons[ii][jj].getOperation(), ii, jj, initialValue);
                neurons[ii][jj].inputNeurons = new LinkedList<>();
                neurons[ii][jj].outputNeurons = new LinkedList<>();
                neurons[ii][jj].setValue(network.getNeurons()[0][0].getValue());
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < inputNeuronsNumber; k++) {
                    int x = network.getNeurons()[i][j].inputNeurons.get(k).getI();
                    int y = network.getNeurons()[i][j].inputNeurons.get(k).getJ();
                    if (y == -1) {
                        connectNeurons(input[x], neurons[i][j]);
                    } else if (y == n) {
                        connectNeurons(neurons[x][y], output[x]);
                    } else {
                        connectNeurons(neurons[x][y], neurons[i][j]);
                    }
                }
                neurons[i][j].value = network.neurons[i][j].value;
            }
        }

        for (int k = 0; k < outputNumber; k++) {
            int i = network.output[k].inputNeurons.getFirst().getI();
            int j = network.output[k].inputNeurons.getFirst().getJ();
            if (j == -1) {
                connectNeurons(input[i], output[k]);
            } else {
                connectNeurons(neurons[i][j], output[k]);
            }
        }

        activeNeurons = new ArrayList<>();
        int i, j;
        for (Neuron<T> x : network.getActiveNeurons()) {
            i = x.getI();
            j = x.getJ();
            if (j == -1) activeNeurons.add(input[i]);
            else if (j == n) activeNeurons.add(output[i]);
            else activeNeurons.add(neurons[i][j]);
        }
    }


    private void addToActiveNeurons(Neuron<T> neuron) {
        int i = neuron.getI(), j = neuron.getJ();
        if (j == -1) {
            neuron = input[i];
            if (!activeNeurons.contains(neuron)) {
                activeNeurons.add(neuron);
            }
        } else {
            if (!activeNeurons.contains(neuron)) {
                activeNeurons.add(neuron);
                for (int kk = 0; kk < inputNeuronsNumber; kk++) {
                    addToActiveNeurons(neuron.getInputNeurons().get(kk));
                }
            }
        }
    }


    private void setOutputActiveNeurons(int num) {
        activeNeurons.add(output[num]);
        addToActiveNeurons(output[num].getInputNeurons().getFirst());
    }

    private void removeConnection(Neuron<T> neuron1, Neuron<T> neuron2) {
        neuron1.getOutputNeurons().remove(neuron2);
        neuron2.getInputNeurons().remove(neuron1);
    }

    private void connectNeurons(Neuron<T> g1, Neuron<T> g2, int position) {
        g2.getInputNeurons().add(position, g1);
        g1.getOutputNeurons().add(g2);
    }

    private void connectNeurons(Neuron<T> g1, Neuron<T> g2) {
        g2.addInputNeurons(g1);
        g1.addOutputNeurons(g2);
    }

    private void setActiveNeurons() {
        activeNeurons.clear();
        for (int ii = 0; ii < output.length; ii++) setOutputActiveNeurons(ii);
        activeNeurons.sort((g1, g2) -> {
            if (g1.getJ() == g2.getJ()) return 0;
            return g1.getJ() > g2.getJ() ? 1 : -1;
        });
    }


    public void printNetwork() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print("(" + neurons[i][j].getI() + "," + neurons[i][j].getJ() + ")=" + neurons[i][j].getOperation() + "	");
            }
            System.out.println();
        }
        System.out.println();

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < inputNeuronsNumber; k++) {
                    int x = neurons[i][j].inputNeurons.get(k).getI();
                    int y = neurons[i][j].inputNeurons.get(k).getJ();
                    System.out.println(x + "," + y + "--->" + "(" + neurons[i][j].getI() + "," + neurons[i][j].getJ() + ")");
                }
                System.out.println();
            }
        }
        System.out.println();

        for (int i = 0; i < outputNumber; i++) {
            int x = output[i].inputNeurons.getFirst().getI();
            int y = output[i].inputNeurons.getFirst().getJ();
            System.out.println(x + "," + y + "--->" + "(" + output[i].getI() + "," + output[i].getJ() + ")");
        }
    }


    private void connectAllNeurons() {
        int randomIi, randomJj;
        double neuronInputProbability;

        for (int j = 0; j < n; j++) {
            neuronInputProbability = (double) inputNumber / (inputNumber + j * m);
            for (int i = 0; i < m; i++) {
                for (int k = 0; k < inputNeuronsNumber; k++) {
                    if (recurrentProbability != 0 && randomGenerator.nextDouble() < recurrentProbability) {
                        randomIi = randomGenerator.nextInt(m);
                        randomJj = j + randomGenerator.nextInt(n - j);
                        connectNeurons(neurons[randomIi][randomJj], neurons[i][j]);
                    } else {
                        if (randomGenerator.nextDouble() < neuronInputProbability || j == 0) {
                            randomIi = randomGenerator.nextInt(inputNumber);
                            connectNeurons(input[randomIi], neurons[i][j]);
                        } else {
                            randomIi = randomGenerator.nextInt(m);
                            randomJj = randomGenerator.nextInt(j);
                            connectNeurons(neurons[randomIi][randomJj], neurons[i][j]);
                        }
                    }
                }
            }
        }

        neuronInputProbability = (double) inputNumber / (inputNumber + n * m);
        for (int ii = 0; ii < outputNumber; ii++) {
            if (randomGenerator.nextDouble() < neuronInputProbability) {
                randomIi = randomGenerator.nextInt(inputNumber);
                connectNeurons(input[randomIi], output[ii]);
            } else {
                randomIi = randomGenerator.nextInt(m);
                randomJj = randomGenerator.nextInt(n);
                connectNeurons(neurons[randomIi][randomJj], output[ii]);
            }
        }

    }


    void reassignNeuronsOperations() {
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                if (randomGenerator.nextDouble() < probability) {
                    neurons[i][j].setOperation(operationList.get(randomGenerator.nextInt(operationList.size())));
                }
            }
        }
    }

    private void relinkNeuron(Neuron<T> neuron) {
        int column, row, j = neuron.getJ();
        double neuronInputProbability = (double) inputNumber / (inputNumber + j * m);
        for (int k = 0; k < inputNeuronsNumber; k++) {
            if (randomGenerator.nextDouble() < probability) {
                removeConnection(neuron.getInputNeurons().get(k), neuron);
                if (recurrentProbability != 0 && randomGenerator.nextDouble() < recurrentProbability) {
                    row = randomGenerator.nextInt(m);
                    column = j + randomGenerator.nextInt(n - j);
                    connectNeurons(neurons[row][column], neuron, k);
                } else {
                    if (randomGenerator.nextDouble() < neuronInputProbability || (j == 0)) {
                        row = randomGenerator.nextInt(inputNumber);
                        connectNeurons(input[row], neuron, k);
                    } else {
                        column = randomGenerator.nextInt(j);
                        row = randomGenerator.nextInt(m);
                        connectNeurons(neurons[row][column], neuron, k);
                    }
                }
            }
        }
    }


    void relinkAllNeurons() {
        for (int j = 0; j < n; j++) for (int i = 0; i < m; i++) relinkNeuron(neurons[i][j]);

        int column, row;
        double inputProbability = (double) inputNumber / (inputNumber + m * n);
        for (int ii = 0; ii < outputNumber; ii++) {
            if (randomGenerator.nextDouble() < probability) {
                removeConnection(output[ii].getInputNeurons().getFirst(), output[ii]);
                if (randomGenerator.nextDouble() < inputProbability) {
                    row = randomGenerator.nextInt(inputNumber);
                    connectNeurons(input[row], output[ii]);
                } else {
                    column = randomGenerator.nextInt(n);
                    row = randomGenerator.nextInt(m);
                    connectNeurons(neurons[row][column], output[ii]);
                }
            }
        }
    }

    public T calculateOutputValue(int arg) {
        setActiveNeurons();
        if (recurrentProbability != 0) {
            for (Neuron<T> neuron : activeNeurons) calculateNeuronValue(neuron);
            return output[arg].getValue();
        } else {
            Neuron<T> lastNeuron = output[arg].getInputNeurons().getFirst();
            return calculateNeuronValue(lastNeuron);
        }
    }

    public void setInputValues(T[] val) {
        if (val.length != inputNumber) return;
        int k = 0;
        for (T x : val) {
            input[k].setValue(x);
            k++;
        }
    }

    private T calculateNeuronValue(Neuron<T> neuron) {
        int i = neuron.getI(), j = neuron.getJ();
        LinkedList<T> valueList = new LinkedList<>();
        LinkedList<Neuron<T>> neuronList;
        if (j == -1) return input[i].getValue();
        else if (j == n) {
            output[i].setValue(output[i].getInputNeurons().getFirst().getValue());
            return output[i].getValue();
        } else {
            neuronList = neurons[i][j].getInputNeurons();
            if (recurrentProbability != 0) for (Neuron<T> x : neuronList) valueList.add(x.getValue());
            else for (Neuron<T> x : neuronList) valueList.add(calculateNeuronValue(x));
        }
        neurons[i][j].setValue(neurons[i][j].getOperation().output(valueList));
        return neurons[i][j].getValue();
    }


    public void calculatesNeuronsValues() {
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                LinkedList<T> values = new LinkedList<T>();
                for (int k = 0; k < inputNeuronsNumber; k++) {
                    T value = neurons[i][j].getInputNeurons().get(k).getValue();
                    values.add(value);
                }
                neurons[i][j].setValue(neurons[i][j].getOperation().output(values));
            }
        }

        for (int jj = 0; jj < outputNumber; jj++)
            output[jj].setValue(output[jj].getInputNeurons().getFirst().getValue());
    }

    int getM() {
        return m;
    }

    int getN() {
        return n;
    }

    public double getRecurrentProbability() {
        return recurrentProbability;
    }

    private double getProbability() {
        return probability;
    }

    Neuron<T>[][] getNeurons() {
        return neurons;
    }

    public void setNeuronsValues(T value) {
        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) neurons[i][j].setValue(value);
    }

    private int getInputNeuronsNumber() {
        return inputNeuronsNumber;
    }

    public int getInputNumber() {
        return inputNumber;
    }

    int getOutputNumber() {
        return outputNumber;
    }

    public Neuron<T>[] getInput() {
        return input;
    }

    public Neuron<T>[] getOutput() {
        return output;
    }

    public void setOutput(Neuron<T>[] output) {
        this.output = output;
    }

    ArrayList<Neuron<T>> getActiveNeurons() {
        return activeNeurons;
    }

    private T getInitialValue() {
        return initialValue;
    }

}
