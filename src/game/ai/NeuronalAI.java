package game.ai;

import game.Model;
import game.Utils;
import game.ai.neuronalnet.Network;
import szte.mi.Move;
import szte.mi.Player;

import java.util.Random;

public class NeuronalAI implements Player {

    private Model model;
    private Network net;

    public NeuronalAI() {
        Utils.printInfo("NeuronalNet AI created");
    }

    public NeuronalAI(String netFile){
        this();
        this.net = Utils.readNetworkFromDisk(netFile);
    }

    @Override
    public void init(int order, long t, Random rnd) {
        Utils.printInfo("NeuronalNet AI init method called [order="+order+"]");
        this.model = new Model(order+1);
    }

    @Override
    public Move nextMove(Move prevMove, long tOpponent, long t) {

        if(prevMove != null){
            long enemyMove = model.getBinaryForCoords(prevMove.x, prevMove.y);
            model.makeMoveEnemy(enemyMove);
        }

        net.setInput(model.boardPlayer, model.boardEnemy);
        long moveBinary = net.getOutput();
        int[] cords = model.getCoordsForBinary(moveBinary);

        return new Move(cords[0], cords[1]);

    }



}
