package game.ai.neuronalnet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Network implements Serializable{

    private Random random;

    private ArrayList<Neuron> inputLayer;
    private ArrayList<Neuron> hiddenLayer;
    private ArrayList<Neuron> outputLayer;

    public Network(){
        inputLayer = new ArrayList<>();
        hiddenLayer = new ArrayList<>();
        outputLayer = new ArrayList<>();
        this.random = new Random();

        initInputLayer();
        initHiddenLayer(64);
        initOutputLayer();
    }

    public void initInputLayer(){
        for (int i = 0; i < 64; i++) {
            inputLayer.add(new InputNeuron(i));
        }
    }
    
    public void initHiddenLayer(int amount){
        for (int i = 0; i < amount; i++) {
            hiddenLayer.add(new HiddenNeuron(random, inputLayer));
        }
    }

    public void initOutputLayer(){
        for (int i = 0; i < 64; i++) {
            outputLayer.add(new HiddenNeuron(random, hiddenLayer));
        }
    }

    public void mutateHiddenLayer(){
        for(Neuron n : hiddenLayer){
            HiddenNeuron hn = (HiddenNeuron) n;
            hn.adjustWeights();
        }
    }

    public void setInput(long boardPlayer, long boardEnemy){
        for (int i = 0; i < 64; i++) {
            int tmp = 0;
            if(((boardPlayer >> i) & 1) == 1){
                tmp = 1;
            }else if(((boardEnemy >> i) & 1) == 1){
                tmp = 2;
            }
            ((InputNeuron)inputLayer.get(i)).setInput(tmp);
        }
    }

    public long getOutput(){
        for (int i = 0; i < outputLayer.size(); i++) {
            HiddenNeuron on = (HiddenNeuron) outputLayer.get(i);
            System.out.println("OutputNeuron at\t"+i+"\t:\t"+on.getOutput());
        }

        return 0L;
    }

    public static void main(String[] args){

        Network net = new Network();

        long boardPlayer = (1L << 28) | (1L << 35);
        long boardEnemy = (1L << 27) | (1L << 36);

        net.setInput(boardPlayer, boardEnemy);
        net.getOutput();
        net.mutateHiddenLayer();
        net.getOutput();

    }

}
