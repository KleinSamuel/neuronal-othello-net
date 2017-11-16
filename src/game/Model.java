package game;

import javafx.util.Pair;

public class Model {

    /* binary representation of black and white chips */
    private long boardBlack;
    private long boardWhite;

    /* list of 8 directions as integer*/
    private int[] directions;

    public Model(){
        initBoard();
    }

    private void initBoard(){
        boardBlack = (1L << 28) | (1L << 35);
        boardWhite = (1L << 27) | (1L << 36);

        directions = new int[]{-1,-9,-8,-7,1,9,8,7};
    }

    private void getPossibleMoves(int player){

        long boardPlayer = (player == 1) ? boardBlack : boardWhite;
        long boardEnemy = (player == 2) ? boardBlack : boardWhite;

        long start = System.currentTimeMillis();

        for (int i = 0; i < 64; i++) {
            if(((boardPlayer >> i) & 1) == 1){
                for (int direction : directions){
                    Pair<Long, Long> out = checkMove(boardPlayer, boardEnemy, i, direction, 0L);
                    if(out.getKey() != 0){
                        System.out.println("Searched "+(i/8)+"-"+(i%8));
                        int[] cords = getCoordsForBinary(out.getKey());
                        System.out.println("Possible move at\t"+cords[0]+"-"+cords[1]);
                        System.out.println();
                    }
                }
            }
        }

        long end = System.currentTimeMillis();

        System.out.println("[ TIME ] Computation of possible moves took "+(end-start)+" milliseconds-");
        
    }

    /**
     * Check if a possible move is found in given direction
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

            toFlip = toFlip | (1 << newPosition);
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
                return (newPosition % 7) == 0;
            case 1:
                return (newPosition % 8) == 0;
            case -9:
                return newPosition < 0 || (newPosition % 7) == 0;
            case 9:
                return newPosition >= 64 || (newPosition % 8) == 0;
            case -7:
                return newPosition < 0 || (newPosition % 8) == 0;
            case 7:
                return newPosition >= 64 || (newPosition % 7) == 0;
            default:
                return false;
        }
    }

    private void move(long position, long toFlip){

    }

    public void printBitBoard() {
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

    public void printCoordsForBinary(long binary){
        int[] cords = getCoordsForBinary(binary);
        System.out.println("BINARY position: ["+cords[0]+"-"+cords[1]+"]");
    }

    public static void main(String[] args){

        Model model = new Model();

        model.printBitBoard();
        model.getPossibleMoves(1);

    }

}
