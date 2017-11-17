package game;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String getBinaryString(Long l){
        String padding = "0000000000000000000000000000000000000000000000000000000000000000";
        String result = padding + Long.toBinaryString(l);
        result = result.substring(result.length() - 64, result.length());
        return result;
    }

    public static String getDateString(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void printInfo(String s){
        System.out.println("[ INFO ]("+getDateString()+")\t"+s);
    }

    public static void printError(String s){
        System.out.println("[ ERROR ]("+getDateString()+")\t"+s);
    }

    public static double sigmoidValue(Double arg) {
        return (1 / (1 + Math.exp(-arg)));
    }

}
