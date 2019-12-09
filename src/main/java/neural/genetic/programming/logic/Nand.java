package neural.genetic.programming.logic;

import java.util.LinkedList;

import neural.genetic.programming.fitness.Operation;


public class Nand implements Operation<Boolean> {
    @Override
    public Boolean output(LinkedList<Boolean> list) {
        return (!list.get(0)) || !(list.get(1));
    }

    @Override
    public String toString() {
        return "NAND";
    }

    @Override
    public int argsNumber() {
        return 2;
    }
}
