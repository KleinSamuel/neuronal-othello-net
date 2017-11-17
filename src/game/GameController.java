package game;

public class GameController {

    public GameController(){

    }

    public static void main(String[] args){

        Game g = new Game(RandomAI.class, RandomAI.class, 8000, 1);
        g.startGameSeries(20);

    }

}
