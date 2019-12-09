package neural.genetic.programming;

import neural.genetic.programming.fitness.NetworkScore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkAlgorithm<T> {
    private int networkNumber;
    private NetworkGenerator<T> networkGenerator;
    private Network network;
    private NetworkScore<T> networkScore;
    private Network result;
    private int times;
    private int fitnessNumber;

    NetworkAlgorithm(NetworkGenerator<T> networkGenerator, Network<T> network, NetworkScore<T> networkScore) {
        this.networkGenerator = networkGenerator;
        this.network = network;
        this.networkScore = networkScore;
        this.networkNumber = NetworkGenerator.getGridNumber();
    }

    public NetworkScore<T> getNetworkScore() {
        return networkScore;
    }

    public void setNetworkScore(NetworkScore<T> networkScore) {
        this.networkScore = networkScore;
    }

    private void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(networkNumber);

        boolean solutionFound = false;
        int[] score = new int[networkNumber + 1];
        int bestGridNumber = 0;
        times = 1;
        score[0] = networkScore.computeNetworkScore(networkGenerator.getMainNetwork());

        while (!solutionFound) {
            score[0] = score[bestGridNumber];
            bestGridNumber = 0;
            System.out.println("==================================");
            System.out.println("Times:	" + times);
            System.out.print(score[0] + "	");
            for (int i = 0; i < networkNumber; i++) {
                score[i + 1] = networkScore.computeNetworkScore(networkGenerator.getNetwork()[i]);
                System.out.print(score[i + 1] + "	");
            }

            for (int i = 1; i < networkNumber; i++) if (score[i] > score[bestGridNumber]) bestGridNumber = i;
            System.out.println(",best score:	" + score[bestGridNumber]);

            if (score[bestGridNumber] == networkScore.computeMaximumScore(networkGenerator.getMainNetwork())) {
                solutionFound = true;
                if (bestGridNumber == 0) result = networkGenerator.getMainNetwork();
                else result = networkGenerator.getNetwork()[bestGridNumber - 1];
            } else {
                if (bestGridNumber == 0) {
                    System.out.println("Network stays, as it is");
                    network = networkGenerator.getMainNetwork();
                    networkGenerator.generateNewGrids();
                } else {
                    network = networkGenerator.getNetwork()[bestGridNumber - 1];
                    networkGenerator = new NetworkGenerator<T>(network);
                    System.out.println("New network with number: " + bestGridNumber);
                }
            }
            times++;

            if (times > 50000) {
                solutionFound = true;
                fitnessNumber = score[bestGridNumber];
                if (bestGridNumber == 0) result = networkGenerator.getMainNetwork();
                else result = networkGenerator.getNetwork()[bestGridNumber - 1];
            }
            System.out.println();
        }
    }


    Network<T> runAlgorithm() {
        run();
        return result;
    }
}