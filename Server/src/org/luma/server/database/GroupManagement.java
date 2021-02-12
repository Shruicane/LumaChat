package org.luma.server.database;

import com.sun.javafx.collections.MappingChange;
import org.luma.server.frontend.controller.Controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupManagement {

    private MySQLConnection mySQLConnection;
    private Controller controller;
    private MySQLDataBase mySQLDataBase;


    public GroupManagement(MySQLConnection mySQLConnection, Controller controller, MySQLDataBase mySQLDataBase) {
        this.mySQLConnection = mySQLConnection;
        this.controller = controller;
        this.mySQLDataBase = mySQLDataBase;
    }

    public String createGroup(String groupName) {

        if (!groupExists(groupName)) {
            String query = "INSERT INTO `groupdata` (`ID`, `Type`, `Name`) VALUES (NULL, '1', '" + groupName + "');\n";
            mySQLDataBase.executeUpdate(query);
        }
        return groupName;
    }

    public void createPrivate(String name1, String name2) {
        String query = "INSERT INTO chatdata (Groupname, Username)\n" +
                "Select '" + name1 + "', '" + name2 + "'\n" +
                "WHERE NOT EXISTS(\n" +
                "SELECT * FROM chatdata WHERE Groupname='" + name1 + "' AND Username='" + name2 + "')";
        mySQLDataBase.executeUpdate(query);
        query = "INSERT INTO chatdata (Groupname, Username)\n" +
                "Select '" + name2 + "', '" + name1 + "'\n" +
                "WHERE NOT EXISTS(\n" +
                "SELECT * FROM chatdata WHERE Groupname='" + name2 + "' AND Username='" + name1 + "')";
        mySQLDataBase.executeUpdate(query);
    }

    public void deletePrivate(String name1, String name2) {
        String query = "DELETE FROM chatdata WHERE Username='" + name1 + "' AND Groupname='" + name2 + "';\n";
        mySQLDataBase.executeUpdate(query);
    }

    public Map<String, String> getUsers() {
        HashMap<String, String> res = new HashMap<>();
        String query = "SELECT * FROM `userdata`";
        ResultSet rs = mySQLDataBase.executeQuery(query);
        try {
            while (rs.next()) {
                res.put(rs.getString("Username"), rs.getString("Password"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }

    public boolean groupExists(String groupName) {
        //Username abgleichen
        String query = "SELECT * FROM `groupdata` WHERE Name=\"" + groupName + "\"";
        ResultSet rs = mySQLDataBase.executeQuery(query);
        try {
            return rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public void deleteGroup(Group group) {
        String query1 = "DELETE FROM groupdata WHERE Name='" + group + "'";
        mySQLDataBase.executeUpdate(query1);
        String query2 = "DELETE FROM chatdata WHERE Groupname='" + group + "'";
        mySQLDataBase.executeUpdate(query2);
    }

    public void addUser(Group group, String username) {
        String query = "INSERT INTO `chatdata` (`ID`, `Groupname`, `Username`) VALUES (NULL, '" + group + "', '" + username + "');\n";
        mySQLDataBase.executeUpdate(query);
    }


    public void removeUser(Group group, String username) {
        String query = "DELETE FROM chatdata WHERE Groupname='" + group + "' AND Username='" + username + "'";
        mySQLDataBase.executeUpdate(query);
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

    public void changeName(String oldName, String name) {
        String query1 = "UPDATE groupdata SET Name = '" + name + "' WHERE Name='" + oldName + "'";
        mySQLDataBase.executeUpdate(query1);
        String query2 = "UPDATE chatdata SET Groupname = '" + name + "' WHERE Groupname='" + oldName + "'";
        mySQLDataBase.executeUpdate(query2);
    }

    public ArrayList<String> getAllGroups() {
        ArrayList<String> res = new ArrayList<>();
        String query = "SELECT * FROM `groupdata`";
        ResultSet rs = mySQLDataBase.executeQuery(query);
        try {
            while (rs.next()) {
                res.add(rs.getString("Name"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }

    public MySQLDataBase getMySQLDataBase() {
        return mySQLDataBase;
    }
}
