package org.luma.client.network;

public class Logger {

    public static String colorize(String str, int color) {
        return (char) 27 + "[" + color + "m" + str + (char) 27 + "[39m";
    }

    public static void network(String str) {
        //green
        String prefix = "[NET] ";
        System.out.println(colorize(prefix + str, 32));
    }

    public static void info(String str) {
        //gray
        String prefix = "[INF] ";
        System.out.println(colorize(prefix + str, 90));
    }

    public static void error(String str) {
        //red
        String prefix = "[ERR] ";
        System.out.println(colorize(prefix + str, 31));
    }

    public static void warning(String str) {
        //yellow
        String prefix = "[WAR] ";
        System.out.println(colorize(prefix + str, 33));
    }

    public static void cmd(String str) {
        //dark white
        String prefix = "[CMD] ";
        System.out.println(colorize(prefix + str, 37));
    }

    public static void message(String str) {
        //white
        String prefix = "[MSG] ";
        System.out.println(colorize(prefix + str, 0));
    }

    public static void system(String str){
        //bright red
        String prefix = "[SYS] ";
        System.out.println(colorize(prefix + str, 91));
    }

    public static void print(String str){
        System.out.println(str);
    }
}
