package org.luma.server.database;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogManager {

    public static final int MESSAGE = 1;
    public static final int ADMINISTRATION = 2;
    public static final int COMMAND = 3;
    public static final int NETWORK = 4;
    public static final int WARNING = 5;
    public static final int ERROR = 6;


    private final MySQLDataBase mySQLDataBase;

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
