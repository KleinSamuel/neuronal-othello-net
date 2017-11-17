package game;

import javafx.util.Pair;
import szte.mi.Move;
import szte.mi.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GreedyAI implements Player {

    private Model model;

    public GreedyAI() {
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

        Pair<Long, Long> playerMove = greedyMove();

        if(playerMove == null){
            return null;
        }

        model.makeMovePlayer(playerMove.getKey(), playerMove.getValue());
        int[] coords = model.getCoordsForBinary(playerMove.getKey());

        return new Move(coords[0], coords[1]);
    }

    private Pair<Long, Long> greedyMove(){
        HashMap<Long, Long> moves = model.getPossibleMoves(model.boardPlayer, model.boardEnemy, false);

        if(moves.size() == 0){
            return null;
        }

        int count = 0;
        Pair<Long, Long> move = null;

        for(Map.Entry<Long, Long> entry : moves.entrySet()){
            int tmpCount = getAmountChipsToFlip(entry.getValue());
            if(tmpCount > count){
                count = tmpCount;
                move = new Pair<>(entry.getKey(), entry.getValue());
            }
        }

        return move;
    }

    private int getAmountChipsToFlip(long toFlip){
        int count = 0;
        for (int i = 0; i < 64; i++) {
            if(((toFlip >> i) & 1) == 1){
                count++;
            }
        }
        return count;
    }
}
