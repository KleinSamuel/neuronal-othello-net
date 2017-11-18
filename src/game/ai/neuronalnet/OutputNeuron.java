package game.ai.neuronalnet;

import game.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class OutputNeuron implements Neuron,Serializable {

    private Random random;

    private ArrayList<Neuron> inputNeurons;

    private ArrayList<Double> weights;

    public OutputNeuron(Random random, ArrayList<Neuron> inputNeurons){
        this.inputNeurons = inputNeurons;
        this.weights = new ArrayList<>();
        this.random = random;
        initWeights();
    }

    public void initWeights(){
        for (int i = 0; i < inputNeurons.size(); i++) {
            weights.add(random.nextDouble());
        }
    }

    private ArrayList<Double> applyWeights(){
        ArrayList<Double> output = new ArrayList<>();
        int count = 0;
        for(Neuron n : inputNeurons){
            output.add(n.getOutput()*weights.get(count));
            count++;
        }
        return output;
    }

    public double getOutput(){
        double sum = 0;
        for(double d : applyWeights()){
            sum += d;
        }
        sum = Utils.sigmoidValue(sum);
        return sum;
    }

}
