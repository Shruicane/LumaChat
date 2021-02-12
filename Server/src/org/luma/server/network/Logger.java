package org.luma.server.network;

import org.luma.server.database.IOManagement;
import org.luma.server.database.LogLevel;
import org.luma.server.database.LogManager;
import org.luma.server.database.MySQLDataBase;
import org.luma.server.frontend.controller.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private final Controller controller;
    private final LogManager logManager;

    public Logger(Controller controller, MySQLDataBase mySQLDataBase){
        this.controller = controller;
        logManager = new LogManager(mySQLDataBase);
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
        String prefixLayout = "t p: ";

        String out = prefixLayout.replace("t", getTime());
        out = out.replace("p", prefix);
        return out;
    }

    public void network(String str) {
        //green
        String prefix = formatPrefix("[NET]");
        System.out.println(colorize(prefix + str, 32));
        controller.updateLogArea(prefix + str);
        logManager.saveLog(str, LogLevel.NETWORK);
    }

    public void info(String str) {
        //gray
        String prefix = formatPrefix("[INF]");
        System.out.println(colorize(prefix + str, 90));
        //controller.updateLogArea(prefix + str);
    }

    public void error(String str) {
        //red
        String prefix = formatPrefix("[ERR]");
        System.out.println(colorize(prefix + str, 31));
        controller.updateLogArea(prefix + str);
        logManager.saveLog(str, LogLevel.ERROR);
    }

    public void warning(String str) {
        //yellow
        String prefix = formatPrefix("[WAR]");
        System.out.println(colorize(prefix + str, 33));
        controller.updateLogArea(prefix + str);
        logManager.saveLog(str, LogLevel.WARNING);
    }

    public void cmd(String str) {
        //dark white
        String prefix = formatPrefix("[CMD]");
        System.out.println(colorize(prefix + str, 37));
        controller.updateLogArea(prefix + str);
    }

    public void message(String str) {
        //white
        String prefix = formatPrefix("[MSG]");
        System.out.println(colorize(prefix + str, 0));
        controller.updateLogArea(prefix + str);
        logManager.saveLog(str, LogLevel.MESSAGE);
    }

    public void administration(String str){
        String prefix = formatPrefix("[ADM]");
        System.out.println(colorize(prefix + str, 0));
        controller.updateLogArea(prefix + str);
        logManager.saveLog(str, LogLevel.ADMINISTRATION);
    }

    public void print(String str) {
        System.out.println(str);
    }

    public LogManager getLogManager() {
        return logManager;
    }
}
