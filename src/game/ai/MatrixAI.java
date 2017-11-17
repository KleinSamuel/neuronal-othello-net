package game.ai;

import game.Model;
import game.Utils;
import javafx.util.Pair;
import szte.mi.Move;
import szte.mi.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MatrixAI implements Player {

    private Model model;

    private int[] posMatrixArray = new int[] {
            100,  -50, 50, 20, 20, 50,  -50, 100 ,
            -50, -100, 10,  5,  5, 10, -100, -50 ,
            50,   10,  8,  1,  1,  8,   10,  50 ,
            20,    5,  1,  0,  0,  1,    5,  20 ,
            20,    5,  1,  0,  0,  1,    5,  20 ,
            50,   10,  8,  1,  1,  8,   10,  50 ,
            -50, -100, 10,  5,  5, 10, -100, -50 ,
            100,  -50, 50, 20, 20, 50,  -50, 100  };

    public MatrixAI() {
        Utils.printInfo("Matrix AI created");
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

        Pair<Long, Long> playerMove = matrixMove();

        if(playerMove == null){
            return null;
        }

        model.makeMovePlayer(playerMove.getKey(), playerMove.getValue());
        int[] coords = model.getCoordsForBinary(playerMove.getKey());

        return new Move(coords[0], coords[1]);
    }

    private Pair<Long, Long> matrixMove(){

        HashMap<Long, Long> moves = model.getPossibleMoves(model.boardPlayer, model.boardEnemy, false);

        if(moves.size() == 0){
            return null;
        }

        int score = -1000;
        Pair<Long, Long> move = null;

        for(Map.Entry<Long, Long> entry : moves.entrySet()){

            int[] cords = model.getCoordsForBinary(entry.getKey());
            int position = cords[0]*8+cords[1];

            if(posMatrixArray[position] > score){
                score = posMatrixArray[position];
                move = new Pair<>(entry.getKey(), entry.getValue());
            }
        }

        return move;
    }
}
