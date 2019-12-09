package neural.genetic.programming;


class NetworkGenerator<T> {
    private final static int gridNumber = 4;
    private Network<T> mainNetwork;
    private Network[] network;

    Network<T> getMainNetwork() {
        return mainNetwork;
    }

    Network[] getNetwork() {
        return network;
    }

	NetworkGenerator(Network<T> initialNetwork) {
        mainNetwork = new Network<T>(initialNetwork);
        network = new Network[gridNumber];
        for (int i = 0; i < gridNumber; i++) network[i] = new Network<>(mainNetwork);
        generateNewGrids();
    }


    void generateNewGrids() {
        for (int i = 0; i < gridNumber; i++) {
            network[i].reassignGatesFunction();
            network[i].relinkAllGates();
        }
    }

    static int getGridNumber() {
        return gridNumber;
    }
}