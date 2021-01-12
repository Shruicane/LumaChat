package org.luma.client.network;


import org.luma.client.frontend.ClientGUI1;
import org.luma.client.frontend.GUI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    GUI gui;

    public Logger(GUI gui){
        this.gui = gui;
    }

    public String colorize(String str, int color) {
        return (char) 27 + "[" + color + "m" + str + (char) 27 + "[39m";
    }

    public String italic(String str) {
        return (char) 27 + "[3m" + str + (char) 27 + "[23m";
    }

    public String bold(String str) {
        return (char) 27 + "[1m" + str + (char) 27 + "[22m";
    }

    public String underline(String str) {
        return (char) 27 + "[4m" + str + (char) 27 + "[24m";
    }

    private String getTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        return dtf.format(LocalDateTime.now());
    }

    private String formatPrefix(String prefix) {
        String prefixLayout = "t ";

        String out = prefixLayout.replace("t", getTime());
        out = out.replace("p", prefix);
        return out;
    }

    public void network(String str) {
        //green
        String prefix = formatPrefix("[NET]");
        System.out.println(colorize(prefix + str, 32));
    }

    public void info(String str) {
        //gray
        String prefix = formatPrefix("[INF]");
        System.out.println(colorize(prefix + str, 90));
    }

    public void error(String str) {
        //red
        String prefix = formatPrefix("[ERR]");
        System.out.println(colorize(prefix + str, 31));
    }

    public void warning(String str) {
        //yellow
        String prefix = formatPrefix("[WAR]");
        System.out.println(colorize(prefix + str, 33));
    }

    public void cmd(String str) {
        //dark white
        String prefix = formatPrefix("[CMD]");
        System.out.println(colorize(prefix + str, 37));
    }

    public void message(String str) {
        //white
        String prefix = formatPrefix("[MSG]");
        System.out.println(colorize(prefix + str, 0));
        gui.updateMessages(prefix + str);
    }

    public void system(String str){
        //bright red
        String prefix = formatPrefix("[SYS]");
        System.out.println(colorize(prefix + str, 91));
    }

    public void print(String str) {
        System.out.println(str);
    }
}
