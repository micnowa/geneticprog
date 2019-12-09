package neural.genetic.programming.mathematics;

import neural.genetic.programming.fitness.Operation;

import java.util.LinkedList;

public class AbsoluteValue implements Operation<Double> {

    @Override
    public Double output(LinkedList<Double> list) {
        if (Double.isNaN(list.get(0)) || Double.isInfinite(list.get(0))) return 1.0;
        return list.get(0) > 0 ? list.get(0) : ((-1) * list.get(0));
    }

    @Override
    public int argsNumber() {
        return 1;
    }

    @Override
    public String toString() {
        return "| |";
    }

}
