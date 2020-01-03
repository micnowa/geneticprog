package neural.genetic.programming;


class NetworkGenerator<T> {
    private final static int networkNumber = 4;
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
        network = new Network[networkNumber];
        for (int i = 0; i < networkNumber; i++) network[i] = new Network<>(mainNetwork);
        generateNewNetworks();
    }


    void generateNewNetworks() {
        for (int i = 0; i < networkNumber; i++) {
            network[i].reassignNeuronsOperations();
            network[i].relinkAllNeurons();
        }
    }

    static int getNetworkNumber() {
        return networkNumber;
    }
}