package neural.genetic.programming.logic;

import java.util.LinkedList;

import neural.genetic.programming.fitness.Operation;


public class Xor implements Operation<Boolean> {
    @Override
    public Boolean output(LinkedList<Boolean> list) {
        return ((list.get(0) || list.get(1)) && !(list.get(0) && list.get(1)));
    }


    @Override
    public String toString() {
        return "XOR";
    }


    @Override
    public int argsNumber() {
        return 2;
    }
}
