package neural.genetic.programming.mathematics;

import java.util.LinkedList;

import neural.genetic.programming.fitness.Operation;

public class Sine implements Operation<Double>
{

	@Override public Double output(LinkedList<Double> list)
	{
		// TODO Auto-generated method stub
		if(Double.isNaN(list.get(0)) || Double.isInfinite(list.get(0))) return 1.0;
		return Math.sin(list.get(0));
	}

	@Override public int argsNumber()
	{
		// TODO Auto-generated method stub
		return 1;
	}

	@Override public String toString()
	{
		return "sin()";
	}

}
