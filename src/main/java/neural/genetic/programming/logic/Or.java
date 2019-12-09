package neural.genetic.programming.logic;

import neural.genetic.programming.fitness.Operation;

import java.util.LinkedList;


public class Or implements Operation<Boolean> {
    @Override
    public Boolean output(LinkedList<Boolean> list) {
        return (list.get(0) || list.get(1));
    }


    @Override
    public String toString() {
        return "OR";
    }


    @Override
    public int argsNumber() {
        return 2;
    }

}
