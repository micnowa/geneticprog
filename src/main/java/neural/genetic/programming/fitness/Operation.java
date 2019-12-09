package neural.genetic.programming.fitness;

import java.util.LinkedList;


public interface Operation<T> {
    T output(LinkedList<T> list);
    int argsNumber();
}