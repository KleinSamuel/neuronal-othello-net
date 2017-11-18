package game;

import game.ai.neuronalnet.Network;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Utils {

    public static final String OUTPUT_DIR = "out/";

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

    public static void storeNetworkOnDisk(Network network){
        String uid = UUID.randomUUID().toString();
        try {
            FileOutputStream fout = new FileOutputStream(OUTPUT_DIR + uid + ".ser");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(network);
            oos.flush();
            oos.close();
            Utils.printInfo("Stored network on disk.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Network readNetworkFromDisk(String path){
        try {
            FileInputStream fin = new FileInputStream(path);
            ObjectInputStream oin = new ObjectInputStream(fin);
            Network net = (Network) oin.readObject();
            oin.close();
            return net;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
