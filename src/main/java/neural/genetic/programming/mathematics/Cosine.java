package neural.genetic.programming.mathematics;

import java.util.LinkedList;

import neural.genetic.programming.fitness.Operation;

public class Cosine implements Operation<Double> {

    @Override
    public Double output(LinkedList<Double> list) {
        boolean isNanInf = Double.isNaN(list.get(0)) || Double.isInfinite(list.get(0));

        return isNanInf ? 1.0 : Math.cos(list.get(0));
    }

    @Override
    public int argsNumber() {
        return 1;
    }

    @Override
    public String toString() {
        return "cos()";
    }

}
