package neural.genetic.programming;

import neural.genetic.programming.fitness.Fitness;

public class FourPlusOneAlgorithm<T> {
    private int gridNumber;
    private NetworkGenerator<T> networkGenerator;
    private Network<T> network;
    private Fitness<T> fitness;
    private Network<T> result;
    private int times;
    private int fitnessNumber;

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    FourPlusOneAlgorithm(NetworkGenerator<T> networkGenerator, Network<T> network, Fitness<T> fitness) {
        this.networkGenerator = networkGenerator;
        this.network = network;
        this.fitness = fitness;
        this.gridNumber = networkGenerator.getGridNumber();
    }


    public NetworkGenerator<T> getNetworkGenerator() {
        return networkGenerator;
    }


    public void setNetworkGenerator(NetworkGenerator<T> networkGenerator) {
        this.networkGenerator = networkGenerator;
    }


    public Network<T> getNetwork() {
        return network;
    }


    public void setNetwork(Network<T> network) {
        this.network = network;
    }


    public Fitness<T> getFitness() {
        return fitness;
    }


    public void setFitness(Fitness<T> fitness) {
        this.fitness = fitness;
    }


    public Network<T> getResult() {
        return result;
    }


    private void run() {
        boolean solutionFound = false;
        int[] score = new int[gridNumber + 1];
        int bestGridNumber = 0;
        times = 1;
        score[0] = fitness.getGridFitness(networkGenerator.getMainNetwork());

        while (!solutionFound) {
            score[0] = score[bestGridNumber];
            bestGridNumber = 0;
            System.out.println("==================================");
            System.out.println("Times runned:	" + times);
            System.out.print(score[0] + "	");
            for (int i = 0; i < gridNumber; i++) {
                score[i + 1] = fitness.getGridFitness(networkGenerator.getNetwork()[i]);
                System.out.print(score[i + 1] + "	");
            }

            for (int i = 1; i < gridNumber; i++) {
                if (score[i] > score[bestGridNumber]) {
                    bestGridNumber = i;
                }
            }
            System.out.println(",best score:	" + score[bestGridNumber]);

            if (score[bestGridNumber] == fitness.getMaxFitness(networkGenerator.getMainNetwork())) {
                solutionFound = true;
                if (bestGridNumber == 0) result = networkGenerator.getMainNetwork();
                else
                    result = networkGenerator.getNetwork()[bestGridNumber - 1];
            } else {
                if (bestGridNumber == 0) {
                    System.out.println("GG stays, as it is");
                    network = networkGenerator.getMainNetwork();
                    System.out.println("New GG with same MainGrid");
                    networkGenerator.generateNewGrids();
                } else {
                    network = networkGenerator.getNetwork()[bestGridNumber - 1];
                    networkGenerator = new NetworkGenerator<T>(network);
                    System.out.println("New GG with grid number: " + bestGridNumber);
                }
            }
            System.out.println("New GG created!");
            times++;

            if (times > 5000) {
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