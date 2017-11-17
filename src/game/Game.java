package game;

import szte.mi.Move;
import szte.mi.Player;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Game {

    private Model model;
    private Random random;

    public int runAmount;

    public Class playerOneClass;
    public Class playerTwoClass;

    public long playerOneTime;
    public long playerTwoTime;

    public Game(Class playerOneClass, Class playerTwoClass, long gameDuration, int runAmount){

        this.random = new Random();

        this.playerOneClass = playerOneClass;
        this.playerTwoClass = playerTwoClass;

        this.playerOneTime = gameDuration/2;
        this.playerTwoTime = gameDuration/2;
        this.runAmount = runAmount;
    }

    public void startGameSeries(int count){

        Player playerOne;
        Player playerTwo;

        HashMap<Integer, Integer> results = new HashMap<>();

        for (int i = 0; i < count; i++) {

            playerOne = getInstance(playerOneClass);
            playerTwo = getInstance(playerTwoClass);

            playerOne.init(0, playerOneTime, random);
            playerTwo.init(1, playerTwoTime, random);

            int tmp = -1;

            try {
                tmp = startNewGame(playerOne, playerTwo, null);
            } catch(IOException e){
                e.printStackTrace();
            }

            if(results.containsKey(tmp)){
                results.put(tmp, results.get(tmp) + 1);
            }else{
                results.put(tmp, 1);
            }

        }

        System.out.println("RESULTS:");
        for(Map.Entry<Integer, Integer> entry : results.entrySet()){
            System.out.println(entry.getKey()+"\t:\t"+entry.getValue());
        }
        
    }

    private Player getInstance(Class playerClass){
        try {
            Constructor<?> ctor = playerClass.getConstructors()[0];
            Player player = (Player) ctor.newInstance(new Object[]{});
            return player;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public int startNewGame(Player playerOne, Player playerTwo, File logfile) throws IOException{

        BufferedWriter bw = null;

        if(logfile != null) {
            bw = new BufferedWriter(new FileWriter(logfile));
        }

        if(bw != null) {
            bw.write("New Game started..");
        }

        long playerOneTimeInternal = playerOneTime;
        long playerTwoTimeInternal = playerTwoTime;

        model = new Model(1);

        int currentPlayer = 1;

        int illegalMove = -1;
        int timedOut = -1;

        Move oldMove = null;
        int oldMoveNullCount = 0;

        while(true){

            /* check if both players returned null */
            if(oldMove == null){
                oldMoveNullCount += 1;
                if(oldMoveNullCount == 2){
                    break;
                }
            }else{
                oldMoveNullCount = 0;
            }

            //model.printBitBoard();

            if(bw != null) {
                bw.write("Current Player:\t" + currentPlayer);
            }

            if(currentPlayer == 1){

                HashMap<Long, Long> possibleMoves = model.getPossibleMoves(model.boardPlayer, model.boardEnemy, false);

                //model.printPossibleMoves(possibleMoves);

                long startTime = System.currentTimeMillis();
                Move move = playerOne.nextMove(oldMove, playerTwoTime, playerOneTime);
                long endTime = System.currentTimeMillis();

                if(move != null) {

                    if(bw != null) {
                        bw.write("Chosen Move:\t[" + move.x + "-" + move.y + "]");
                    }

                    long moveAsBinary = model.getBinaryForCoords(move.x, move.y);
                    long timeTook = endTime - startTime;

                    if (!possibleMoves.containsKey(moveAsBinary)) {
                        illegalMove = 1;
                        break;
                    }

                    if (playerOneTimeInternal > timeTook) {
                        playerOneTimeInternal -= timeTook;
                    } else {
                        timedOut = 1;
                        break;
                    }

                    //model.printToFlip(possibleMoves.get(moveAsBinary));

                    model.makeMovePlayer(moveAsBinary, possibleMoves.get(moveAsBinary));

                }

                oldMove = move;

            }else{

                HashMap<Long, Long> possibleMoves = model.getPossibleMoves(model.boardEnemy, model.boardPlayer, false);

                //model.printPossibleMoves(possibleMoves);

                long startTime = System.currentTimeMillis();
                Move move = playerTwo.nextMove(oldMove, playerOneTime, playerTwoTime);
                long endTime = System.currentTimeMillis();

                if(move != null) {

                    if(bw != null) {
                        bw.write("Chosen Move:\t[" + move.x + "-" + move.y + "]");
                    }

                    long moveAsBinary = model.getBinaryForCoords(move.x, move.y);
                    long timeTook = endTime - startTime;

                    if (!possibleMoves.containsKey(moveAsBinary)) {
                        illegalMove = 0;
                        break;
                    }

                    if (playerTwoTimeInternal > timeTook) {
                        playerTwoTimeInternal -= timeTook;
                    } else {
                        timedOut = 0;
                        break;
                    }

                    //model.printToFlip(possibleMoves.get(moveAsBinary));

                    model.makeMoveEnemy(moveAsBinary);

                }

                oldMove = move;

            }

            if(currentPlayer == 1){
                currentPlayer = 0;
            }else{
                currentPlayer = 1;
            }

        }

        /* check if a player made an illegal move, 1 = playerOne, 0 = playerTwo */
        if(illegalMove != -1){
            if(illegalMove == 1){
                if(bw != null) {
                    bw.write("Player One made an illegal move!");
                }
                return 2;
            }else{
                if(bw != null) {
                    bw.write("Player Two made an illegal move!");
                }
                return 1;
            }
        }

        /* check if a player timed out, 1 = playerOne, 0 = playerTwo */
        if(timedOut != -1){
            if(timedOut == 1){
                if(bw != null) {
                    bw.write("Player One timed out!");
                }
                return 2;
            }else{
                if(bw != null) {
                    bw.write("Player Two timed out!");
                }
                return 1;
            }
        }

        int winner = model.getWinner(model.boardPlayer, model.boardEnemy);

        if(bw != null) {
            bw.write("THE WINNER IS " + winner);
        }

        //model.printBitBoard();

        return winner;

    }

}
