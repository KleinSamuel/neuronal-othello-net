package game.ai;

import game.Model;
import game.Utils;
import javafx.util.Pair;
import szte.mi.Move;
import szte.mi.Player;

import java.util.HashMap;
import java.util.Random;

public class RandomAI implements Player {

    private Model model;
    private Random rnd;

    public RandomAI() {
        Utils.printInfo("Random AI created");
    }

    @Override
    public void init(int order, long t, Random rnd) {
        Utils.printInfo("Random AI init method called [order="+order+"]");
        this.model = new Model(order+1);
        this.rnd = rnd;
    }

    @Override
    public Move nextMove(Move prevMove, long tOpponent, long t) {

        if(prevMove != null){
            long enemyMove = model.getBinaryForCoords(prevMove.x, prevMove.y);
            model.makeMoveEnemy(enemyMove);
        }

        Pair<Long, Long> playerMove = randomMove(rnd);

        if(playerMove == null){
            return null;
        }

        model.makeMovePlayer(playerMove.getKey(), playerMove.getValue());
        int[] coords = model.getCoordsForBinary(playerMove.getKey());

        return new Move(coords[0], coords[1]);
    }

    private Pair<Long, Long> randomMove(Random rnd){
        HashMap<Long, Long> moves = model.getPossibleMoves(model.boardPlayer, model.boardEnemy, false);

        if(moves.size() == 0){
            return null;
        }

        Object[] keys = moves.keySet().toArray();
        long randomKey = (long) keys[rnd.nextInt(keys.length)];
        return new Pair<>(randomKey, moves.get(randomKey));
    }
}
