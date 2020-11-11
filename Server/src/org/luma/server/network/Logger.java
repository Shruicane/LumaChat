package org.luma.server.network;

public class Logger {

    public static String colorize(String str, int color) {
        return (char) 27 + "[" + color + "m" + str + (char) 27 + "[39m";
    }

    public static String italic(String str) {
        return (char) 27 + "[3m" + str + (char) 27 + "[23m";
    }

    public static String bold(String str) {
        return (char) 27 + "[1m" + str + (char) 27 + "[22m";
    }

    public static String underline(String str) {
        return (char) 27 + "[4m" + str + (char) 27 + "[24m";
    }

    public static void network(String str) {
        //green
        String prefix = Logger.bold("[NET] ");
        System.out.println(colorize(prefix + str, 32));
    }

    public static void info(String str) {
        //gray
        String prefix = Logger.bold("[INF] ");
        System.out.println(colorize(prefix + str, 90));
    }

    public static void error(String str) {
        //red
        String prefix = Logger.bold("[ERR] ");
        System.out.println(colorize(prefix + str, 31));
    }

    public static void warning(String str) {
        //yellow
        String prefix = Logger.bold("[WAR] ");
        System.out.println(colorize(prefix + str, 33));
    }

    public static void cmd(String str) {
        //dark white
        String prefix = Logger.bold("[CMD] ");
        System.out.println(colorize(prefix + str, 37));
    }

    public static void message(String str) {
        //white
        String prefix = Logger.bold("[MSG] ");
        System.out.println(colorize(prefix + str, 0));
    }

    public static void print(String str) {
        System.out.println(str);
    }
}
