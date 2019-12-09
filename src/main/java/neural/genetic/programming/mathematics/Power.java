package neural.genetic.programming.mathematics;

import neural.genetic.programming.fitness.Operation;

import java.util.LinkedList;

public class Power implements Operation<Double> {

    @Override
    public Double output(LinkedList<Double> list) {
        if (Double.isNaN(list.get(0)) || Double.isInfinite(list.get(0))) return 1.0;
        if (Double.isNaN(list.get(1)) || Double.isInfinite(list.get(1))) return 1.0;
        double power;
        try {
            power = Math.pow(list.get(0), list.get(1));
        } catch (ArithmeticException e) {
            return 1.0;
        }
        if (Double.isNaN(power) || Double.isInfinite(power)) return 1.0;
        return power;
    }

    @Override
    public int argsNumber() {
        return 2;
    }

    @Override
    public String toString() {
        return "^";
    }
}
