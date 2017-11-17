package game;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Model {

    /* binary representation of black and white chips */
    public long boardPlayer;
    public long boardEnemy;

    /* list of 8 directions as integer*/
    private int[] directions;

    /**
     * @param player 1 if player is black, 0 if player is white
     */
    public Model(int player){
        initBoard(player);
    }

    /**
     * Init the board
     *
     * @param player 1 if player is black, 0 if player is white
     */
    private void initBoard(int player){

        if(player == 1){
            boardPlayer = (1L << 28) | (1L << 35);
            boardEnemy = (1L << 27) | (1L << 36);
        }else{
            boardEnemy = (1L << 28) | (1L << 35);
            boardPlayer = (1L << 27) | (1L << 36);
        }

        directions = new int[]{-1,-9,-8,-7,1,9,8,7};
    }

    public HashMap<Long, Long> getPossibleMoves(long boardPlayer, long boardEnemy, boolean print){

        if(print) {
            System.out.println("###");
        }

        long start = System.currentTimeMillis();

        HashMap<Long, Long> moveMap = new HashMap<>();

        for (int i = 0; i < 64; i++) {
            if(((boardPlayer >> i) & 1) == 1){
                for (int direction : directions){

                    if(print){
                        int[] co = getCoordsForBinary((1L << i));
                        System.out.println("check direction "+direction+" for position ["+co[0]+"-"+co[1]+"]");
                    }

                    Pair<Long, Long> out = checkMove(boardPlayer, boardEnemy, i, direction, 0L);

                    if(print) {
                        System.out.println(Utils.getBinaryString(out.getKey()));
                    }

                    if(out.getKey() != 0){

                        int[] cords = getCoordsForBinary(out.getKey());

                        if(print) {
                            System.out.println("[" + cords[0] + "-" + cords[1] + "]");
                        }

                        if(moveMap.containsKey(out.getKey())){
                            long oldToFlip = moveMap.get(out.getKey());
                            long newToFlip = out.getValue();
                            long sumToFlip = oldToFlip | newToFlip;
                            moveMap.put(out.getKey(), sumToFlip);
                        }else{
                            moveMap.put(out.getKey(), out.getValue());
                        }
                    }
                }
            }
        }

        if(print) {
            System.out.println("###");
        }

        long end = System.currentTimeMillis();

        //System.out.println("[ TIME ] Computation of possible moves took "+(end-start)+" milliseconds");

        return moveMap;
    }

    /**
     * Check if a possible move is found in given direction
     *
     * TODO: Maybe remove Pair and encode position into existing long
     *
     * @param boardPlayer binary rep of player chips
     * @param boardEnemy binary rep of enemy chips
     * @param position current search position
     * @param direction current direction as
     * @param toFlip binary rep of enemy chips to flip
     * @return Pair of Long with 0L if no move is possible, binary rep of position for move and binary
     *         rep of chips to flip for this move otherwise
     */
    private Pair<Long, Long> checkMove(long boardPlayer, long boardEnemy, long position, int direction, long toFlip){

        long newPosition = position + direction;

        if(isOutOfBounds(newPosition, direction)){
            return new Pair<>(0L, 0L);
        }

        if(((boardEnemy >> newPosition) & 1) == 1){

            toFlip = toFlip | (1L << newPosition);
            return checkMove(boardPlayer, boardEnemy, newPosition, direction, toFlip);

        }else if(((boardPlayer >> newPosition) & 1) == 1){

            return new Pair<>(0L, 0L);

        }else{

            if(toFlip != 0){
                long tmp = (1L << newPosition);
                return new Pair<>(tmp, toFlip);
            }else{
                return new Pair<>(0L, 0L);
            }
        }
    }

    /**
     * Check if current position is out of bounds for given direction
     *
     * @param newPosition current position on the board
     * @param direction current direction to check
     * @return true if out of bounds
     */
    private boolean isOutOfBounds(long newPosition, int direction){
        switch (direction){
            case -8:
                return newPosition < 0;
            case 8:
                return newPosition >= 64;
            case -1:
                return ((newPosition+1) % 8) == 0;
            case 1:
                return (newPosition % 8) == 0;
            case -9:
                return newPosition < 0 || ((newPosition+1) % 8) == 0;
            case 9:
                return newPosition >= 64 || (newPosition % 8) == 0;
            case -7:
                return newPosition < 0 || (newPosition % 8) == 0;
            case 7:
                return newPosition >= 64 || ((newPosition+1) % 8) == 0;
            default:
                return false;
        }
    }

    private Pair<Long, Long> move(long boardPlayer, long boardEnemy, long position, long toFlip){

        boardPlayer = boardPlayer | position;
        boardPlayer = boardPlayer | toFlip;
        boardEnemy = boardEnemy ^ toFlip;

        return new Pair<>(boardPlayer, boardEnemy);
    }

    public void makeMovePlayer(long move, long toFlip){
        Pair<Long, Long> result = move(boardPlayer, boardEnemy, move, toFlip);
        boardPlayer = result.getKey();
        boardEnemy = result.getValue();
    }

    public void makeMoveEnemy(long move){

        HashMap<Long, Long> enemyMoves = getPossibleMoves(boardEnemy, boardPlayer, false);

        if(enemyMoves.containsKey(move)){
            Long toFlip = enemyMoves.get(move);
            Pair<Long, Long> result = move(boardEnemy, boardPlayer, move, toFlip);
            boardEnemy = result.getKey();
            boardPlayer = result.getValue();
        }else{
            Utils.printError("Given move not in possible moves!");
        }
    }

    /**
     * Check if game is over. Either if no free space is available or if no player
     * possible moves
     *
     * @param boardPlayer
     * @param boardEnemy
     * @return
     */
    public boolean gameOver(long boardPlayer, long boardEnemy){

        if((boardPlayer | boardEnemy) == -1){
            Utils.printInfo("Board is completely full.");
            return true;
        }

        return false;

        /*HashMap<Long, Long> playerMoves = getPossibleMoves(boardPlayer, boardEnemy, false);
        HashMap<Long, Long> enemyMoves = getPossibleMoves(boardEnemy, boardPlayer, false);

        if(playerMoves.size() == 0 || enemyMoves.size() == 0){
            Utils.printInfo("No Move possible.");
            return true;
        }

        return false;*/
    }

    /**
     * Get the winner of given board
     *
     * @param boardPlayer
     * @param boardEnemy
     * @return 1 if player, 2 if enemy, 0 if draw
     */
    public int getWinner(long boardPlayer, long boardEnemy){
        int playerCount = 0;
        int enemyCount = 0;
        for (int i = 0; i < 64; i++) {
            if(((boardPlayer >> i) & 1) == 1){
                playerCount ++;
            }
            if(((boardEnemy >> i) & 1) == 1){
                enemyCount ++;
            }
        }
        if(playerCount > enemyCount){
            return 1;
        }else if(playerCount < enemyCount){
            return 2;
        }else{
            return 0;
        }
    }

    /**
     * Print the given board state
     */
    public void printBitBoard(long boardBlack, long boardWhite) {
        System.out.println("---------------------------------");
        System.out.print("|");
        for (int i = 0; i < 64; i++) {
            if (((boardBlack >> i) & 1) == 1) {
                System.out.print(" X |");
            }else if (((boardWhite >> i) & 1) == 1) {
                System.out.print(" O |");
            }else{
                System.out.print("   |");
            }
            if(((i+1)%8) == 0){
                System.out.println();
                System.out.println("---------------------------------");
                System.out.print((i < 63)?"|":"");
            }
        }
    }

    /**
     * Print the current board state
     */
    public void printBitBoard(){
        printBitBoard(boardPlayer, boardEnemy);
    }

    /**
     * Transform a binary to x and y coordinates
     *
     * @param binary coordinate on board encoded as binary
     * @return int[0] = x, int[1] = y
     */
    public int[] getCoordsForBinary(long binary){
        if ((binary & (binary-1)) == 0){
            for (int i = 0; i < 64; i++) {
                if(((binary >> i) & 1) == 1){
                    int x = i/8;
                    int y = i%8;
                    return new int[]{x,y};
                }
            }
        }
        return null;
    }

    /**
     * Get the binary representation for a x and y coordinate
     *
     * @param x coordinate
     * @param y coordinate
     * @return
     */
    public long getBinaryForCoords(int x, int y){
        int position = x*8+y;
        return (1L << position);
    }

    /**
     * Print the x and y coordinates of a given binary encoded position
     * @param binary
     */
    public void printCoordsForBinary(long binary){
        int[] cords = getCoordsForBinary(binary);
        System.out.println("BINARY position: ["+cords[0]+"-"+cords[1]+"]");
    }

    public void printPossibleMoves(HashMap<Long, Long> moveMap){
        System.out.println("Possible Moves:");
        for(Map.Entry<Long, Long> entry : moveMap.entrySet()){
            int[] cords = getCoordsForBinary(entry.getKey());
            System.out.println("["+cords[0]+"-"+cords[1]+"]");
        }
    }

    public void printToFlip(long toFlip){
        System.out.println("Positions to flip:");
        for (int i = 0; i < 64; i++) {
            if(((toFlip >> i) & 1) == 1){
                int[] cords = getCoordsForBinary((1L << i));
                System.out.println("["+cords[0]+"-"+cords[1]+"]");
            }
        }
    }

    public static void main(String[] args){

        Model model = new Model(1);
        model.printBitBoard();

        model.makeMovePlayer((1L << 19), (1L << 27));

        model.printBitBoard();

        Pair<Long, Long> move = model.checkMove(model.boardEnemy, model.boardPlayer, 36, -1, 0L);

        System.out.println(Utils.getBinaryString(move.getKey()));

        //HashMap<Long, Long> movesEnemy = model.getPossibleMoves(model.boardEnemy, model.boardPlayer, false);

        //model.printPossibleMoves(movesEnemy);

    }

}
