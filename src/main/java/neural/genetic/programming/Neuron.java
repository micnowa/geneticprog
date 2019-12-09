package neural.genetic.programming;

import neural.genetic.programming.fitness.Operation;

import java.util.LinkedList;

public class Neuron<T> {
    private int i;
    private int j;
    private Operation<T> operation;
    T value;
    LinkedList<Neuron<T>> enteringNeurons;
    LinkedList<Neuron<T>> exitingNeurons;


    Neuron() {
        enteringNeurons = new LinkedList<>();
        exitingNeurons = new LinkedList<>();
    }


    Neuron(Operation<T> operation, int ii, int jj, T initialValue) {
        this.operation = operation;
        this.i = ii;
        this.j = jj;
        this.value = initialValue;
        enteringNeurons = new LinkedList<>();
        exitingNeurons = new LinkedList<>();
    }


    public void setOperation(Operation<T> operation) {
        this.operation = operation;
    }


    public Operation<T> getOperation() {
        return operation;
    }


    void setI(int i) {
        this.i = i;
    }


    int getI() {
        return i;
    }


    void setJ(int j) {
        this.j = j;
    }

    int getJ() {
        return j;
    }

    LinkedList<Neuron<T>> getEnteringNeurons() {
        return enteringNeurons;
    }

    public LinkedList<Neuron<T>> getExitingNeurons() {
        return exitingNeurons;
    }

    T getValue() {
        return value;
    }

    void setValue(T value) {
        this.value = value;
    }

    void addEnteringGate(Neuron<T> neuron) {
        enteringNeurons.add(neuron);
    }

    void addExitingGate(Neuron<T> neuron) {
        exitingNeurons.add(neuron);
    }
}