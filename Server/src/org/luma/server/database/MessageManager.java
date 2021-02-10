package org.luma.server.database;

import org.luma.server.frontend.controller.Controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MessageManager {

    private MySQLDataBase mySQLDataBase;


    public MessageManager(MySQLDataBase mySQLDataBase) {
        this.mySQLDataBase = mySQLDataBase;
    }

    public void saveMessage(String message, String groupUUID, String date, String time, String senderName){
        String query = "INSERT INTO `messages` (`GroupUUID`, `Message`, `Date`, `Time`, `Sender`) " +
                "VALUES ('" + groupUUID + "', '" + message + "', '" + date + "', '" + time + "', '" + senderName + "')";
        mySQLDataBase.executeUpdate(query);
    }

    public void deleteMessage(String message, String groupUUID, String senderName){
        String query = "DELETE FROM messages WHERE GroupUUID='" + groupUUID + "' AND Message='" + message + "' AND Sender='" + senderName + "'\n";
        mySQLDataBase.executeUpdate(query);
    }

    public ArrayList<String> getAllMessages(String groupUUID){
        ArrayList<String> res = new ArrayList<>();
        String query = "SELECT * FROM `messages` WHERE GroupUUID=\"" + groupUUID + "\"";
        ResultSet rs = mySQLDataBase.executeQuery(query);
        try {
            while (rs.next()){
                String msg = rs.getString("Message");
                String date = rs.getString("Date");
                String time = rs.getString("Time");
                String sender = rs.getString("Sender");
                res.add("" + date + " " + time + " " + sender + " : " + msg);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }

    public ArrayList<String> getAllPrivateMessages(String user1, String user2){
        ArrayList<String> res = new ArrayList<>();
        String query =
                "SELECT Message, Time, Sender, GroupUUID\n" +
                "FROM messages\n" +
                "WHERE Sender='"+user1+"' AND GroupUUID='"+user2+"' OR Sender='"+user2+"' AND GroupUUID='"+user1+"'";
        ResultSet rs = mySQLDataBase.executeQuery(query);
        try {
            while (rs.next()) {
                String msg = rs.getString("Message");
                String time = rs.getString("Time");
                String sender = rs.getString("Sender");
                res.add(time + " " + sender + " : " + msg);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }

}
