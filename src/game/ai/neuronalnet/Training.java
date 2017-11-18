package game.ai.neuronalnet;

import game.Game;
import game.Model;

public class Training {

    private Model model;
    private Network net;

    public void startGame(Class neuronalAI, Class otherAI, int duration){
        Game game = new Game(neuronalAI, otherAI, 60000, 1);
    }

    public static void main(String[] args){



    }

}
