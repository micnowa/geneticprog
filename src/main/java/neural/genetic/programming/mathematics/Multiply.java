package neural.genetic.programming.mathematics;

import java.util.LinkedList;

import neural.genetic.programming.fitness.Operation;

public class Multiply implements Operation<Double> {

    @Override
    public Double output(LinkedList<Double> list) {
        boolean isInfNan = (Double.isNaN(list.get(0)) || Double.isInfinite(list.get(0)))
                || (Double.isNaN(list.get(1)) || Double.isInfinite(list.get(1)));
        return isInfNan ? 1.0 : list.get(0) * list.get(1);
    }

    @Override
    public int argsNumber() {
        return 2;
    }

    @Override
    public String toString() {
        return "*";
    }

}
