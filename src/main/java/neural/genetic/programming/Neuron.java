package neural.genetic.programming;

import java.util.LinkedList;

import neural.genetic.programming.fitness.Operation;


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

    public int getEnteringGatesNumber() {
        return enteringNeurons.size();
    }


    public void setEnteringNeurons(LinkedList<Neuron<T>> gatesEntering) {
        this.enteringNeurons = gatesEntering;
    }


    public LinkedList<Neuron<T>> getEnteringNeurons() {
        return enteringNeurons;
    }


    public int getExitingGatesNumber() {
        return exitingNeurons.size();
    }


    public LinkedList<Neuron<T>> getExitingNeurons() {
        return exitingNeurons;
    }


    public void setExitingNeurons(LinkedList<Neuron<T>> gatesExiting) {
        this.exitingNeurons = gatesExiting;
    }


    public T getValue() {
        return value;
    }


    public void setValue(T value) {
        this.value = value;
    }


    void addEnteringGate(Neuron<T> neuron) {
        enteringNeurons.add(neuron);
    }


    void addEnteringGateAt(int pos, Neuron<T> neuron) {
        enteringNeurons.add(pos, neuron);
    }


    void addExitingGate(Neuron<T> neuron) {
        exitingNeurons.add(neuron);
    }

}