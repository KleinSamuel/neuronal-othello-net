package game;

import game.ai.MatrixAI;
import game.ai.RandomAI;

public class GameController {

    public static void main(String[] args){

        Game g = new Game(RandomAI.class, MatrixAI.class, 8000, 1);
        long a = System.currentTimeMillis();
        g.startGameSeries(10000000);
        System.out.println(System.currentTimeMillis()-a);

    }

}
