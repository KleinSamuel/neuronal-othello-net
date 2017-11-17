package game.ai.neuronalnet;

import game.Model;
import game.Utils;
import szte.mi.Move;
import szte.mi.Player;

import java.util.Random;

public class NeuronalAI implements Player {

    private Model model;

    public NeuronalAI() {
        Utils.printInfo("Greedy AI created");
    }

    @Override
    public void init(int order, long t, Random rnd) {
        Utils.printInfo("Random AI init method called [order="+order+"]");
        this.model = new Model(order+1);
    }

    @Override
    public Move nextMove(Move prevMove, long tOpponent, long t) {

        if(prevMove != null){
            long enemyMove = model.getBinaryForCoords(prevMove.x, prevMove.y);
            model.makeMoveEnemy(enemyMove);
        }

        return null;

    }



}
