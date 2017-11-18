package game.ai.neuronalnet;

import com.dkriesel.snipe.core.NeuralNetwork;
import com.dkriesel.snipe.core.NeuralNetworkDescriptor;

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
            outputLayer.add(new OutputNeuron(random, hiddenLayer));
        }
    }

    public void mutateHiddenLayer(){
        for(Neuron n : hiddenLayer){
            HiddenNeuron hn = (HiddenNeuron) n;
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

        int outputPosition = -1;
        double score = -1;

        for (int i = 0; i < outputLayer.size(); i++) {
            OutputNeuron on = (OutputNeuron) outputLayer.get(i);
            //System.out.println("OutputNeuron at\t"+i+"\t:\t"+on.getOutput());
            if(on.getOutput() > score){
                score = on.getOutput();
                outputPosition = i;
            }
        }
        return (1L << outputPosition);
    }

    public ArrayList<Double> getOutputAsList(){
        ArrayList<Double> list = new ArrayList<>();
        for (int i = 0; i < outputLayer.size(); i++) {
            OutputNeuron on = (OutputNeuron) outputLayer.get(i);
            list.add(on.getOutput());
        }
        return list;
    }

    public ArrayList<Double> makeMove(long boardPlayer, long boardEnemy){
        setInput(boardPlayer, boardEnemy);
        ArrayList<Double> move = getOutputAsList();
        return move;
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
