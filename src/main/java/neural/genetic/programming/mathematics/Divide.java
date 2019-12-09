package neural.genetic.programming.mathematics;

import java.util.LinkedList;

import neural.genetic.programming.fitness.Operation;

public class Divide implements Operation<Double> {

    @Override
    public Double output(LinkedList<Double> list) {
        if (Double.isNaN(list.get(0)) || Double.isInfinite(list.get(0))) return 1.0;
        if (Double.isNaN(list.get(1)) || Double.isInfinite(list.get(1))) return 1.0;

        if (list.get(1) == 0) return 1.0;
        double quotient = list.get(0) / list.get(1);
        if (Double.isNaN(quotient) || Double.isInfinite(quotient)) return 1.0;
        return quotient;
    }

    @Override
    public int argsNumber() {
        return 2;
    }

    @Override
    public String toString() {
        return "/";
    }

}
