package neural.genetic.programming.fitness;
import neural.genetic.programming.Network;

public interface NetworkScore<T> {
    int computeNetworkScore(Network<T> network);
    int computeMaximumScore(Network<T> network);
}