package neural.genetic.programming;

import java.awt.*;
import java.util.LinkedList;


public class GridVisualizer<T> extends Canvas {

    private static final long serialVersionUID = 1L;


    private Network<T> network;


    private int a;


    private int dist;


    public GridVisualizer(Network<T> network) {
        this.network = network;
    }


    public int getA() {
        return a;
    }


    public void setA(int a) {
        this.a = a;
    }


    public int getDist() {
        return dist;
    }


    public void setDist(int d) {
        this.dist = d;
    }


    public void paint(Graphics g) {
        g.drawString("Grid", 40, 40);
        setBackground(Color.WHITE);
        dist = 150;
        a = 30;
        LinkedList<Neuron<T>> neuronList;
        for (int ii = 0; ii < network.getM(); ii++)// Draw Gates and links between
        {
            for (int jj = 0; jj < network.getN(); jj++) {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect((jj + 1) * dist, (ii + 1) * dist, a, a);
                g.setColor(Color.RED);
                g.drawString(ii + "	" + jj, (jj + 1) * dist, (ii + 1) * dist);
                g.setColor(Color.BLUE);
                g.drawString(network.getNeurons()[ii][jj].getOperation().toString(), (jj + 1) * dist, (ii + 1) * dist + a / 2);
                g.setColor(Color.LIGHT_GRAY);
                neuronList = network.getNeurons()[ii][jj].getEnteringNeurons();
                for (Neuron<T> tNeuron : neuronList) {
                    if (tNeuron.getJ() == -1) {
                        g.drawLine((jj + 1) * dist, (ii + 1) * dist + a / 2, (tNeuron.getJ() + 1) * dist + a, tNeuron.getI() * dist + a + 80 - a / 2);
                    } else {
                        g.drawLine((jj + 1) * dist, (ii + 1) * dist + a / 2, (tNeuron.getJ() + 1) * dist + a, (tNeuron.getI() + 1) * dist + a / 2);
                    }
                }
            }
        }
        g.setColor(Color.RED);
        for (int ii = 0; ii < network.getInputNumber(); ii++)// Draw Input
        {
            g.fillRect(0, ii * dist + 80, a, a);
            g.setColor(Color.GRAY);
            g.drawString(network.getInput()[ii].getValue().toString(), 0, ii * dist + 80);
            g.setColor(Color.RED);
        }

        for (int ii = 0; ii < network.getOutputNumber(); ii++)// Draw Output
        {
            g.setColor(Color.ORANGE);
            g.fillRect(1500, ii * dist + 80, a, a);
            g.setColor(Color.RED);
            g.drawString(network.getOutput()[ii].getValue().toString(), 1500, ii * dist + 80);
            g.setColor(Color.LIGHT_GRAY);
            int gateI = network.getOutput()[ii].getEnteringNeurons().getFirst().getI();
            int gateJ = network.getOutput()[ii].getEnteringNeurons().getFirst().getJ();
            if (network.getOutput()[ii].getEnteringNeurons().getFirst().getJ() == -1) {
                g.drawLine(a, gateI * dist + 80 + a / 2, 1500, ii * dist + 50 + a + a / 2);
            } else {
                g.drawLine((gateJ + 1) * dist + a, (gateI + 1) * dist + a / 2, 1500, ii * dist + 50 + a + a / 2);
            }
        }

        // Highlighting output
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.RED);
        int gateI, gateJ;
        for (int ii = 0; ii < network.getOutputNumber(); ii++) {
            gateI = network.getOutput()[ii].getEnteringNeurons().getFirst().getI();
            gateJ = network.getOutput()[ii].getEnteringNeurons().getFirst().getJ();
            if (network.getOutput()[ii].getEnteringNeurons().getFirst().getJ() == -1) {
                g.drawLine(a, gateI * dist + 80 + a / 2, 1500, ii * dist + 50 + a + a / 2);
            } else {
                g.drawLine((gateJ + 1) * dist + a, (gateI + 1) * dist + a / 2, 1500, ii * dist + 50 + a + a / 2);
            }
        }

        int ii, jj;
        for (Neuron<T> neuron : network.getActiveNeurons()) {
            ii = neuron.getI();
            jj = neuron.getJ();
            if (jj != network.getN()) highlightGate(g2, ii, jj);
        }
    }


    public void highlightGate(Graphics2D g, int ii, int jj) {
        if (jj == -1) return;
        g.setColor(Color.CYAN);
        g.fillRect((jj + 1) * dist, (ii + 1) * dist, a, a);
        g.setColor(Color.BLUE);
        g.drawString(network.getNeurons()[ii][jj].getOperation().toString(), (jj + 1) * dist, (ii + 1) * dist + a / 2);
        Neuron<T> neuron = network.getNeurons()[ii][jj];
        LinkedList<Neuron<T>> neuronList = neuron.getEnteringNeurons();
        for (int kk = 0; kk < neuronList.size(); kk++) {
            if (neuronList.get(kk).getJ() == -1) {
                g.setColor(Color.GREEN);
                g.drawLine((jj + 1) * dist, (ii + 1) * dist + a / 2, (neuronList.get(kk).getJ() + 1) * dist + a, neuronList.get(kk).getI() * dist + a + 80 - a / 2);
            } else {
                g.setColor(Color.BLACK);
                g.drawLine((jj + 1) * dist, (ii + 1) * dist + a / 2, (neuronList.get(kk).getJ() + 1) * dist + a, (neuronList.get(kk).getI() + 1) * dist + a / 2);
            }
        }
    }
}