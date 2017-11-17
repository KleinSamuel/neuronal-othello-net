package game.ai.neuronalnet;

import java.util.ArrayList;

public class OutputNeuron implements Neuron {

    private ArrayList<Neuron> inputNeurons;

    private ArrayList<Double> weights;

    public OutputNeuron(ArrayList<Neuron> inputNeurons){
        this.inputNeurons = inputNeurons;
        this.weights = new ArrayList<>();
    }

    public double getOutput() {
        double sum = 0;
        for(Neuron n : inputNeurons){
            sum += n.getOutput();
        }
        return sum;
    }
}
