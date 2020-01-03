package neural.genetic.programming.logic;

import neural.genetic.programming.Network;
import neural.genetic.programming.fitness.NetworkScore;

public class LogicAdderScore implements NetworkScore<Boolean> {
    @Override
    public int computeNetworkScore(Network<Boolean> network) {
        int inputNumber = network.getInputNumber();
        int possibilities = (int) Math.pow(2, inputNumber);
        int score = 0;
        Boolean[] tab;

        for (int j = 0; j < inputNumber; j++) if (network.getInput()[j].getOutputNeurons().size() == 0) return 0;
        for (int i = 0; i < possibilities; i++) {
            tab = bytesTable(inputNumber, i);
            network.setInputValues(tab);
            if (network.getRecurrentProbability() != 0) network.setNeuronsValues(false);
            network.calculatesNeuronsValues();
            if (i != (possibilities - 1) && !network.calculateOutputValue(0)) score++;
            else if (i == (possibilities - 1) && network.calculateOutputValue(0)) score++;
        }

        return score;
    }

    private Boolean[] bytesTable(int N, int ii) {
        Boolean[] tab = new Boolean[N];
        String s = Integer.toString(ii, 2);
        if (s.length() != N) {
            StringBuilder sPlus = new StringBuilder();
            int len = N - s.length();
            for (int j = 0; j < len; j++) sPlus.append("0");
            s = sPlus + s;
        }
        for (int j = 0; j < N; j++) tab[j] = s.charAt(j) == '1';
        return tab;
    }

    @Override
    public int computeMaximumScore(Network<Boolean> network) {
        return (int) Math.pow(2, network.getInputNumber());
    }
}