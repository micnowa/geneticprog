package neural.genetic.programming;

import neural.genetic.programming.fitness.Operation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;


public class Network<T> implements Serializable {
    private int m;
    private int n;
    private int enteringGatesNumber;
    private Neuron<T>[][] neurons;
    private int inputNumber;
    private Neuron<T>[] input;
    private int outputNumber;
    private Neuron<T>[] output;

    private Random randomGenerator;

    private LinkedList<Operation<T>> functionList;


    private double recurrentProbability;


    private double probability;


    private ArrayList<Neuron<T>> activeNeurons;


    private T initialValue;


    public Network(LinkedList<Operation<T>> functions, int inputNum, int outputNum, int m, int n, T initialValue, double probability, double recurrentProbability) {
        this.m = m;
        this.n = n;
        this.neurons = new Neuron[m][n];
        this.randomGenerator = new Random();
        this.recurrentProbability = recurrentProbability;
        this.probability = probability;
        this.initialValue = initialValue;

        this.inputNumber = inputNum;
        this.input = new Neuron[inputNumber];
        for (int ii = 0; ii < inputNumber; ii++) {
            this.input[ii] = new Neuron<T>();
            this.input[ii].setI(ii);
            this.input[ii].setJ(-1);
            this.input[ii].exitingNeurons = new LinkedList<Neuron<T>>();
        }

        this.outputNumber = outputNum;
        this.output = new Neuron[outputNumber];
        for (int ii = 0; ii < outputNumber; ii++) {
            this.output[ii] = new Neuron<T>();
            this.output[ii].setI(ii);
            this.output[ii].setJ(n);
            this.output[ii].enteringNeurons = new LinkedList<Neuron<T>>();
        }

        this.functionList = new LinkedList<Operation<T>>();
        this.enteringGatesNumber = functions.get(0).argsNumber();
        for (Operation<T> func : functions) {
            this.functionList.add(func);
            if (func.argsNumber() > this.enteringGatesNumber) this.enteringGatesNumber = func.argsNumber();
        }
        for (int jj = 0; jj < n; jj++) {
            for (int ii = 0; ii < m; ii++) {
                this.neurons[ii][jj] = new Neuron<T>(this.functionList.get(this.randomGenerator.nextInt(this.functionList.size())), ii, jj, initialValue);
            }
        }

        this.activeNeurons = new ArrayList<Neuron<T>>();
        linkAllGates();
    }


    Network(Network<T> network) {
        m = network.m;
        n = network.n;
        randomGenerator = new Random();
        enteringGatesNumber = network.getEnteringGatesNumber();
        functionList = new LinkedList<Operation<T>>(network.functionList);
        for (Operation<T> func : network.functionList) {
            functionList.add(func);
        }
        inputNumber = network.inputNumber;
        outputNumber = network.outputNumber;
        probability = network.getProbability();
        recurrentProbability = network.getRecurrentProbability();
        initialValue = network.getInitialValue();

        input = new Neuron[inputNumber];
        for (int ii = 0; ii < inputNumber; ii++) {
            input[ii] = new Neuron<T>(null, ii, -1, initialValue);
            input[ii].value = network.input[ii].value;
            input[ii].exitingNeurons = new LinkedList<Neuron<T>>();
        }

        output = new Neuron[outputNumber];
        for (int ii = 0; ii < outputNumber; ii++) {
            output[ii] = new Neuron<T>(null, ii, n, initialValue);
            output[ii].enteringNeurons = new LinkedList<Neuron<T>>();
        }

        neurons = new Neuron[m][n];
        for (int ii = 0; ii < m; ii++) {
            for (int jj = 0; jj < n; jj++) {
                neurons[ii][jj] = new Neuron<T>(network.neurons[ii][jj].getOperation(), ii, jj, initialValue);
                neurons[ii][jj].enteringNeurons = new LinkedList<Neuron<T>>();
                neurons[ii][jj].exitingNeurons = new LinkedList<Neuron<T>>();
                neurons[ii][jj].setValue(network.getNeurons()[0][0].getValue());
            }
        }

        for (int ii = 0; ii < m; ii++) {
            for (int jj = 0; jj < n; jj++) {
                for (int kk = 0; kk < enteringGatesNumber; kk++) {
                    int xx = network.getNeurons()[ii][jj].enteringNeurons.get(kk).getI();
                    int yy = network.getNeurons()[ii][jj].enteringNeurons.get(kk).getJ();
                    if (yy == -1) {
                        linkGates(input[xx], neurons[ii][jj]);
                    } else if (yy == n) {
                        linkGates(neurons[xx][yy], output[xx]);
                    } else {
                        linkGates(neurons[xx][yy], neurons[ii][jj]);
                    }
                }
                neurons[ii][jj].value = network.neurons[ii][jj].value;
            }
        }

        for (int kk = 0; kk < outputNumber; kk++) {
            int ii = network.output[kk].enteringNeurons.getFirst().getI();
            int jj = network.output[kk].enteringNeurons.getFirst().getJ();
            if (jj == -1) {
                linkGates(input[ii], output[kk]);
            } else {
                linkGates(neurons[ii][jj], output[kk]);
            }
        }

        activeNeurons = new ArrayList<Neuron<T>>();
        int ii, jj;
        for (Neuron<T> x : network.getActiveNeurons()) {
            ii = x.getI();
            jj = x.getJ();
            if (jj == -1) activeNeurons.add(input[ii]);
            else if (jj == n) activeNeurons.add(output[ii]);
            else
                activeNeurons.add(neurons[ii][jj]);
        }
    }


    int getM() {
        return m;
    }


    public void setM(int m) {
        this.m = m;
    }


    int getN() {
        return n;
    }


    public void setN(int n) {
        this.n = n;
    }


    public double getRecurrentProbability() {
        return recurrentProbability;
    }


    public void setRecurrentProbability(double recurrentProbability) {
        this.recurrentProbability = recurrentProbability;
    }


    private double getProbability() {
        return probability;
    }


    public void setProbability(double probability) {
        this.probability = probability;
    }


    Neuron<T>[][] getNeurons() {
        return neurons;
    }


    public void setGatesValue(T value) {
        for (int ii = 0; ii < m; ii++)
            for (int jj = 0; jj < n; jj++)
                neurons[ii][jj].setValue(value);
    }


    public int getEnteringGatesNumber() {
        return enteringGatesNumber;
    }


    public void setEnteringGatesNumber(int enteringGatesNumber) {
        this.enteringGatesNumber = enteringGatesNumber;
    }


    public LinkedList<Operation<T>> getFunctionList() {
        return functionList;
    }


    public void setFunctionList(LinkedList<Operation<T>> functionList) {
        this.functionList = functionList;
    }


    public int getInputNumber() {
        return inputNumber;
    }


    public void setInputNumber(int inputNumber) {
        this.inputNumber = inputNumber;
    }


    int getOutputNumber() {
        return outputNumber;
    }


    public void setOutputNumber(int outputNumber) {
        this.outputNumber = outputNumber;
    }


    public Neuron<T>[] getInput() {
        return input;
    }


    public void setInput(Neuron<T>[] input) {
        this.input = input;
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


    public void setInitialValue(T initialValue) {
        this.initialValue = initialValue;
    }


    private void addToActiveGates(Neuron<T> neuron) {
        int ii = neuron.getI(), jj = neuron.getJ();
        if (jj == -1) {
            neuron = input[ii];
            if (!activeNeurons.contains(neuron)) {
                activeNeurons.add(neuron);
            }
        } else {
            if (!activeNeurons.contains(neuron)) {
                activeNeurons.add(neuron);
                for (int kk = 0; kk < enteringGatesNumber; kk++) {
                    addToActiveGates(neuron.getEnteringNeurons().get(kk));
                }
            }
        }
    }


    private void setOutputActiveGates(int num) {
        activeNeurons.add(output[num]);
        addToActiveGates(output[num].getEnteringNeurons().getFirst());
    }


    private void setActiveGates() {
        activeNeurons.clear();
        for (int ii = 0; ii < output.length; ii++)
            setOutputActiveGates(ii);
        activeNeurons.sort((g1, g2) -> {
            if (g1.getJ() == g2.getJ()) return 0;
            return g1.getJ() > g2.getJ() ? 1 : -1;
        });
    }


    public void printGrid() {
        for (int ii = 0; ii < m; ii++) {
            for (int jj = 0; jj < n; jj++) {
                System.out.print("(" + neurons[ii][jj].getI() + "," + neurons[ii][jj].getJ() + ")=" + neurons[ii][jj].getOperation() + "	");
            }
            System.out.println();
        }
        System.out.println();

        for (int ii = 0; ii < m; ii++) {
            for (int jj = 0; jj < n; jj++) {
                for (int kk = 0; kk < enteringGatesNumber; kk++) {
                    int i = neurons[ii][jj].enteringNeurons.get(kk).getI();
                    int j = neurons[ii][jj].enteringNeurons.get(kk).getJ();
                    System.out.println(i + "," + j + "--->" + "(" + neurons[ii][jj].getI() + "," + neurons[ii][jj].getJ() + ")");
                }
                System.out.println();
            }
        }
        System.out.println();

        for (int ii = 0; ii < outputNumber; ii++) {
            int i = output[ii].enteringNeurons.getFirst().getI();
            int j = output[ii].enteringNeurons.getFirst().getJ();
            System.out.println(i + "," + j + "--->" + "(" + output[ii].getI() + "," + output[ii].getJ() + ")");
        }
    }


    private void linkGates(Neuron<T> g1, Neuron<T> g2) {
        g2.addEnteringGate(g1);
        g1.addExitingGate(g2);

    }


    private void linkGates(Neuron<T> g1, Neuron<T> g2, int position) {
        g2.getEnteringNeurons().add(position, g1);
        g1.getExitingNeurons().add(g2);
    }


    private void removeLink(Neuron<T> g1, Neuron<T> g2) {
        g1.getExitingNeurons().remove(g2);
        g2.getEnteringNeurons().remove(g1);
    }


    private void linkAllGates() {
        int randomIi, randomJj;
        double gateInputProbability;

        for (int jj = 0; jj < n; jj++) {
            gateInputProbability = (double) inputNumber / (inputNumber + jj * m);
            for (int ii = 0; ii < m; ii++) {
                for (int kk = 0; kk < enteringGatesNumber; kk++) {
                    if (recurrentProbability != 0 && randomGenerator.nextDouble() < recurrentProbability) {
                        randomIi = randomGenerator.nextInt(m);
                        randomJj = jj + randomGenerator.nextInt(n - jj);
                        linkGates(neurons[randomIi][randomJj], neurons[ii][jj]);
                    } else {
                        if (randomGenerator.nextDouble() < gateInputProbability || jj == 0) {
                            randomIi = randomGenerator.nextInt(inputNumber);
                            linkGates(input[randomIi], neurons[ii][jj]);
                        } else {
                            randomIi = randomGenerator.nextInt(m);
                            randomJj = randomGenerator.nextInt(jj);
                            linkGates(neurons[randomIi][randomJj], neurons[ii][jj]);
                        }
                    }
                }
            }
        }

        gateInputProbability = (double) inputNumber / (inputNumber + n * m);
        for (int ii = 0; ii < outputNumber; ii++) {
            if (randomGenerator.nextDouble() < gateInputProbability) {
                randomIi = randomGenerator.nextInt(inputNumber);
                linkGates(input[randomIi], output[ii]);
            } else {
                randomIi = randomGenerator.nextInt(m);
                randomJj = randomGenerator.nextInt(n);
                linkGates(neurons[randomIi][randomJj], output[ii]);
            }
        }

    }


    private void relinkGate(Neuron<T> neuron) {
        int column, row, jj = neuron.getJ();
        double gateInputProbability = (double) inputNumber / (inputNumber + jj * m);
        for (int kk = 0; kk < enteringGatesNumber; kk++) {
            if (randomGenerator.nextDouble() < probability) {
                removeLink(neuron.getEnteringNeurons().get(kk), neuron);

                if (recurrentProbability != 0 && randomGenerator.nextDouble() < recurrentProbability) {
                    row = randomGenerator.nextInt(m);
                    column = jj + randomGenerator.nextInt(n - jj);
                    linkGates(neurons[row][column], neuron, kk);
                } else {
                    if (randomGenerator.nextDouble() < gateInputProbability || (jj == 0)) {
                        row = randomGenerator.nextInt(inputNumber);
                        linkGates(input[row], neuron, kk);
                    } else {
                        column = randomGenerator.nextInt(jj);
                        row = randomGenerator.nextInt(m);
                        linkGates(neurons[row][column], neuron, kk);
                    }
                }
            }
        }
    }


    void reassignGatesFunction() {
        for (int jj = 0; jj < n; jj++) {
            for (int ii = 0; ii < m; ii++) {
                if (randomGenerator.nextDouble() < probability) {
                    neurons[ii][jj].setOperation(functionList.get(randomGenerator.nextInt(functionList.size())));
                }
            }
        }
    }


    protected void relinkAllGates() {
        for (int jj = 0; jj < n; jj++) for (int ii = 0; ii < m; ii++) relinkGate(neurons[ii][jj]);

        int column, row;
        double inputProbability = (double) inputNumber / (inputNumber + m * n);
        for (int ii = 0; ii < outputNumber; ii++) {
            if (randomGenerator.nextDouble() < probability) {
                removeLink(output[ii].getEnteringNeurons().getFirst(), output[ii]);
                if (randomGenerator.nextDouble() < inputProbability) {
                    row = randomGenerator.nextInt(inputNumber);
                    linkGates(input[row], output[ii]);
                } else {
                    column = randomGenerator.nextInt(n);
                    row = randomGenerator.nextInt(m);
                    linkGates(neurons[row][column], output[ii]);
                }
            }
        }
    }


    public void setInputValues(T[] val) {
        if (val.length != inputNumber) return;
        int kk = 0;
        for (T x : val) {
            input[kk].setValue(x);
            kk++;
        }
    }


    public void calculateValueForEveryGate() {
        for (int jj = 0; jj < n; jj++) {
            for (int ii = 0; ii < m; ii++) {
                LinkedList<T> values = new LinkedList<T>();
                for (int kk = 0; kk < enteringGatesNumber; kk++) {
                    T value = neurons[ii][jj].getEnteringNeurons().get(kk).getValue();
                    values.add(value);
                }
                neurons[ii][jj].setValue(neurons[ii][jj].getOperation().output(values));
            }
        }

        for (int jj = 0; jj < outputNumber; jj++) {
            output[jj].setValue(output[jj].getEnteringNeurons().getFirst().getValue());
        }
    }


    private T calculateGateValue(Neuron<T> neuron) {
        int ii = neuron.getI(), jj = neuron.getJ();
        LinkedList<T> valueList = new LinkedList<T>();
        LinkedList<Neuron<T>> neuronList;
        if (jj == -1) {
            return input[ii].getValue();
        } else if (jj == n) {
            output[ii].setValue(output[ii].getEnteringNeurons().getFirst().getValue());
            return output[ii].getValue();
        } else {
            neuronList = neurons[ii][jj].getEnteringNeurons();
            if (recurrentProbability != 0) {
                for (Neuron<T> x : neuronList) {
                    valueList.add(x.getValue());
                }
            } else {
                for (Neuron<T> x : neuronList) {
                    valueList.add(calculateGateValue(x));
                }
            }
        }
        neurons[ii][jj].setValue(neurons[ii][jj].getOperation().output(valueList));

        return neurons[ii][jj].getValue();
    }


    public T calculateOutputValue(int arg) {
        setActiveGates();
        if (recurrentProbability != 0) {
            for (Neuron<T> neuron : activeNeurons) {
                calculateGateValue(neuron);
            }
            return output[arg].getValue();
        } else {
            Neuron<T> lastNeuron = output[arg].getEnteringNeurons().getFirst();
            return calculateGateValue(lastNeuron);
        }
    }


    public void clearGatesValues() {
        for (int ii = 0; ii < m; ii++) for (int jj = 1; jj < n; jj++) neurons[ii][jj].setValue(initialValue);
        for (int ii = 0; ii < inputNumber; ii++) input[ii].setValue(initialValue);
        for (int ii = 0; ii < outputNumber; ii++) output[ii].setValue(initialValue);
    }

}
