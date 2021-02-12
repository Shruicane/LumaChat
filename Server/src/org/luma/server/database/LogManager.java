package org.luma.server.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LogManager {

    private final MySQLDataBase mySQLDataBase;

    public LogManager(MySQLDataBase mySQLDataBase) {
        this.mySQLDataBase = mySQLDataBase;
    }

    public void saveLog(String logMsg, LogLevel logLevel){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String datum = formatter.format(date).split(" ")[0];
        String time = formatter.format(date).split(" ")[1];


        String query = "INSERT INTO `logdata` (`LogMessage`, `LogLvl`, `Datum`, `Uhrzeit`) " +
                "VALUES ('" + logMsg+ "', '" + logLevel.getLogLvl() + "', '" + datum + "', '" + time + "');";
        mySQLDataBase.executeUpdate(query);
    }

    public ArrayList<String> getLogs(int amountOfEntries){
        ArrayList<String> result = new ArrayList<>();
        int count = 0;
        String amountQuery = "SELECT COUNT(*) as logCount FROM logdata";
        ResultSet rs = mySQLDataBase.executeQuery(amountQuery);

        try {
            rs.next();
            count = rs.getInt("logCount");
            System.out.println("CCCOunt: " + count);
            String listQuery = "SELECT * FROM logdata WHERE ID >= " + (count - amountOfEntries + 1);
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
