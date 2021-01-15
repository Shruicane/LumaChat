package org.luma.server.database;

import jdk.jfr.internal.LogLevel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogManager {

    private MySQLDataBase mySQLDataBase;

    public LogManager(MySQLDataBase mySQLDataBase) {
        this.mySQLDataBase = mySQLDataBase;
    }

    public void saveLog(String logMsg, int logLevel){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String datum = formatter.format(date).split(" ")[0];
        String time = formatter.format(date).split(" ")[1];


        String query = "INSERT INTO `logdata` (`LogMessage`, `LogLvl`, `Datum`, `Uhrzeit`) " +
                "VALUES ('" + logMsg+ "', '" + logLevel + "', '" + datum + "', '" + time + "');";
        mySQLDataBase.executeUpdate(query);
    }

}
