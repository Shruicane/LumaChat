package org.luma.server.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public ArrayList<String> getLogs(int amountOfEntries){
        ArrayList<String> result = new ArrayList<>();
        int count = 0;
        String amountQuery = "SELECT COUNT(*) FROM logdata";
        ResultSet rs = mySQLDataBase.executeQuery(amountQuery);

        try {
            count = rs.getInt("COUNT(*)");
            String listQuery = "SELECT * FROM `logdata` WHERE `ID` >= " + (count - amountOfEntries);
            rs = mySQLDataBase.executeQuery(listQuery);

            String msg = "";
            while (rs.next()){
                LogLevel logLevel = LogLevel.resolveLogLvl(rs.getInt("LogLvl"));
                msg = rs.getString("Datum") + " " + rs.getString("Uhrzeit") + " [" + logLevel.toString() + "] >> " +
                        rs.getString("LogMessage");
                result.add(msg);
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return result;
    }

}
