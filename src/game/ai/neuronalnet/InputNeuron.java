package game.ai.neuronalnet;

public class InputNeuron implements Neuron{

    private int position;

    private int input;

    public InputNeuron(int position){
        this.position = position;
    }

    /**
     * Set input, 0 = free, 1 = black, 2 = white
     *
     * @param input
     */
    public void setInput(int input){
        this.input = input;
    }

    public double getOutput(){
        return this.input;
    }

}
