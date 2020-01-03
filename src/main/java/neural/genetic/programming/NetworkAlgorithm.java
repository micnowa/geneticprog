package neural.genetic.programming;

import neural.genetic.programming.fitness.NetworkScore;

class NetworkAlgorithm<T> {
    private int networkNumber;
    private NetworkGenerator<T> networkGenerator;
    private Network network;
    private NetworkScore<T> networkScore;
    private Network result;

    NetworkAlgorithm(NetworkGenerator<T> networkGenerator, Network<T> network, NetworkScore<T> networkScore) {
        this.networkGenerator = networkGenerator;
        this.network = network;
        this.networkScore = networkScore;
        this.networkNumber = NetworkGenerator.getNetworkNumber();
    }

    private void run() {
        boolean solutionFound = false;
        int[] score = new int[networkNumber + 1];
        int bestNetworkIndex = 0;
        int iteration = 1;
        score[0] = networkScore.computeNetworkScore(networkGenerator.getMainNetwork());

        while (!solutionFound) {
            score[0] = score[bestNetworkIndex];
            bestNetworkIndex = 0;
            System.out.println("Iteration:	" + iteration);
            System.out.print(score[0] + "	");
            for (int i = 0; i < networkNumber; i++) {
                score[i + 1] = networkScore.computeNetworkScore(networkGenerator.getNetwork()[i]);
                System.out.print(score[i + 1] + "	");
            }

            for (int i = 1; i < networkNumber; i++) if (score[i] > score[bestNetworkIndex]) bestNetworkIndex = i;
            System.out.println(",best score:	" + score[bestNetworkIndex]);

            if (score[bestNetworkIndex] == networkScore.computeMaximumScore(networkGenerator.getMainNetwork())) {
                solutionFound = true;
                if (bestNetworkIndex == 0) result = networkGenerator.getMainNetwork();
                else result = networkGenerator.getNetwork()[bestNetworkIndex - 1];
            } else {
                if (bestNetworkIndex == 0) {
                    network = networkGenerator.getMainNetwork();
                    networkGenerator.generateNewNetworks();
                } else {
                    network = networkGenerator.getNetwork()[bestNetworkIndex - 1];
                    networkGenerator = new NetworkGenerator<T>(network);
                    System.out.println("New network with number: " + bestNetworkIndex);
                }
            }
            iteration++;

            if (iteration > 50000) {
                solutionFound = true;
                if (bestNetworkIndex == 0) result = networkGenerator.getMainNetwork();
                else result = networkGenerator.getNetwork()[bestNetworkIndex - 1];
            }
            System.out.println();
        }
    }


    Network<T> runAlgorithm() {
        run();
        return result;
    }
}