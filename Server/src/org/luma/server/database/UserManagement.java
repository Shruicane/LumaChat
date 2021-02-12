package org.luma.server.database;

import org.luma.server.frontend.controller.Controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserManagement {

    private final MySQLConnection mySQLConnection;
    private final Controller controller;
    private final MySQLDataBase mySQLDataBase;

    public Controller getController() {
        return controller;
    }

    public UserManagement(MySQLConnection mySQLConnection, Controller controller, MySQLDataBase mySQLDataBase) {
        this.mySQLDataBase = mySQLDataBase;
        this.mySQLConnection = mySQLConnection;
        this.controller = controller;
    }

    public boolean createUser(String username, String password) {
        String query = "INSERT INTO `userdata` (`UUID`, `Username`, `Password`, `Chatlist`, `BanStatus`) VALUES ('" + UUID.randomUUID() + "', '" + username + "', '" + password + "', '', '0')";

        if (!userExists(username)) {
            mySQLDataBase.executeUpdate(query);
        } else {
            return false;
        }
        //Check if Username already exists
        //create Database entry
        return true;
    }

    public boolean loginUser(String username, String password) {

        //Password und Username abgleichen
        String query = "SELECT * FROM `userdata` WHERE Username=\"" + username + "\" AND Password=\"" + password + "\"";
        ResultSet rs = mySQLDataBase.executeQuery(query);
        try {
            return rs.next() && getBanStatus(username) != BanStatus.PERMABAN;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean userExists(String username) {
        //Username abgleichen
        String query = "SELECT * FROM `userdata` WHERE Username=\"" + username + "\"";
        ResultSet rs = mySQLDataBase.executeQuery(query);
        try {
            return rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public void permaBanUser(String username) {
        String query = "UPDATE userdata SET BanStatus = 3  WHERE Username=\"" + username + "\"";
        this.removeBan(username);
        mySQLDataBase.executeUpdate(query);
    }

    public void tempBanUser(String username, String expiringDate) {
        String query = "UPDATE userdata SET BanStatus = 2, BanExpiry=\"" + expiringDate + "\" WHERE Username=\"" + username + "\"";
        this.removeBan(username);
        mySQLDataBase.executeUpdate(query);
    }

    public void removeBan(String username) {
        String query = "UPDATE userdata SET BanStatus = 0 AND BanExpiry=\"\" WHERE Username=\"" + username + "\"";
        mySQLDataBase.executeUpdate(query);
    }

    public void warnUser(String username) {
        String query = "UPDATE userdata SET BanStatus = 1  WHERE Username=\"" + username + "\"";
        this.removeBan(username);
        mySQLDataBase.executeUpdate(query);
    }

    public boolean deleteUser(String username) {
        if (userExists(username)) {
            String query = "DELETE FROM userdata WHERE Username='" + username + "'";
            mySQLDataBase.executeUpdate(query);
        } else {
            return false;
        }
        return true;
    }

    public BanStatus getBanStatus(String username) {
        String query = "SELECT * FROM `userdata` WHERE Username=\"" + username + "\"";
        ResultSet rs = mySQLDataBase.executeQuery(query);
        try {
            rs.next();
            int status = rs.getInt("BanStatus");
            switch (status) {
                case 0:
                    return BanStatus.NONE;
                case 1:
                    return BanStatus.WARNED;
                case 2:
                    return BanStatus.TEMPBAN;
                case 3:
                    return BanStatus.PERMABAN;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return BanStatus.NONE;
    }

    public ArrayList<String> getAllGroups(String username) {
        ArrayList<String> res = new ArrayList<>();
        String query =
                "SELECT chatdata.Groupname\n" +
                        "FROM chatdata\n" +
                        "LEFT JOIN groupdata ON chatdata.Groupname = groupdata.Name WHERE chatdata.Username = '"+username+"' AND groupdata.ID IS NOT NULL";
        ResultSet rs = mySQLDataBase.executeQuery(query);
        try {
            while (rs.next()) {
                res.add(rs.getString("Groupname"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }

    public ArrayList<String> getAllUsers(String groupName) {
        ArrayList<String> res = new ArrayList<>();
        String query = "SELECT * FROM `chatdata` WHERE Groupname=\"" + groupName + "\"";
        ResultSet rs = mySQLDataBase.executeQuery(query);
        try {
            while (rs.next()) {
                res.add(rs.getString("Username"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }

    public Map<String, ArrayList<String>> getAllGroupsWithUser(String username) {
        Map<String, ArrayList<String>> result = new HashMap<>();
        ArrayList<String> groups = getAllGroups(username);
        for (String groupName : groups) {
            result.put(groupName, getAllUsers(groupName));
        }
        return result;
    }

    public Map<String, ArrayList<String>> getAllChatsFromUser(String username) {
        Map<String, ArrayList<String>> result = new HashMap<>();
        String query =
                "SELECT chatdata.Groupname\n" +
                "FROM chatdata\n" +
                "LEFT JOIN groupdata ON chatdata.Groupname = groupdata.Name WHERE chatdata.Username = '"+username+"' AND groupdata.ID IS NULL";
        ResultSet rs = mySQLDataBase.executeQuery(query);
        try {
            while (rs.next()) {
                String user = rs.getString("Groupname");
                result.put(user, mySQLConnection.getMessageManager().getAllPrivateMessages(user, username));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }

    public ArrayList<String> getAllUsers(){
        ArrayList<String> res = new ArrayList<>();
        String query = "SELECT * FROM `userdata`";
        ResultSet rs = mySQLDataBase.executeQuery(query);
        try {
            while (rs.next()) {
                res.add(rs.getString("Username"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;

    }
}
