package neural.genetic.programming.mathematics;

import java.util.LinkedList;

import neural.genetic.programming.fitness.Operation;

public class Cube implements Operation<Double> {

    @Override
    public Double output(LinkedList<Double> list) {
        boolean isInfNan = (Double.isNaN(list.get(0)) || Double.isInfinite(list.get(0)));
        return isInfNan ? 1.0 : list.get(0) * list.get(0) * list.get(0);
    }

    @Override
    public int argsNumber() {
        return 1;
    }

    @Override
    public String toString() {
        return "^3";
    }

}
