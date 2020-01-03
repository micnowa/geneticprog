package neural.genetic.programming;

import neural.genetic.programming.fitness.Operation;

import java.util.LinkedList;

public class Neuron<T> {
    private int i;
    private int j;
    private Operation<T> operation;
    LinkedList<Neuron<T>> inputNeurons;
    LinkedList<Neuron<T>> outputNeurons;
    T value;


    Neuron() {
        inputNeurons = new LinkedList<>();
        outputNeurons = new LinkedList<>();
    }


    Neuron(Operation<T> operation, int i, int j, T initialValue) {
        this.operation = operation;
        this.i = i;
        this.j = j;
        this.value = initialValue;
        inputNeurons = new LinkedList<>();
        outputNeurons = new LinkedList<>();
    }

    public void setOperation(Operation<T> operation) { this.operation = operation; }

    public Operation<T> getOperation() { return operation; }

    void setI(int i) { this.i = i; }

    int getI() { return i; }

    void setJ(int j) { this.j = j; }

    int getJ() { return j; }

    LinkedList<Neuron<T>> getInputNeurons() { return inputNeurons; }

    public LinkedList<Neuron<T>> getOutputNeurons() { return outputNeurons; }

    T getValue() { return value; }

    void setValue(T value) { this.value = value; }

    void addInputNeurons(Neuron<T> neuron) { inputNeurons.add(neuron); }

    void addOutputNeurons(Neuron<T> neuron) { outputNeurons.add(neuron); }
}