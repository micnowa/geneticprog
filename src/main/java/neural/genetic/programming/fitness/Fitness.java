package neural.genetic.programming.fitness;
import neural.genetic.programming.Network;

public interface Fitness<T> {
    int getGridFitness(Network<T> network);
    int getMaxFitness(Network<T> network);
}