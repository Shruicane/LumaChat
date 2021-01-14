package org.luma.server.database;

import org.luma.server.frontend.controller.Controller;

import java.util.*;

public class UserManagement {

    private MySQLConnection mySQLConnection;
    private Controller controller;

    private DatabaseTMP database;

    public UserManagement(MySQLConnection mySQLConnection, Controller controller) {
        this.mySQLConnection = mySQLConnection;
        this.controller = controller;
        this.database = mySQLConnection.getDatabase();
    }

    public boolean exists(String username){
        return database.existsUser(username);
    }

    public boolean createUser(String username, String password){
        if(!exists(username)){
            database.addUser(username, password);
            controller.updateUser(username, password, false);
            return true;
        }
        return false;
    }

    public boolean deleteUser(String username){
        if(exists(username)) {
            database.removeUser(username);
            return true;
        }
        return false;
    }

    public boolean loginUser(String username, String password){
        return database.checkPassword(username, password) && !database.isBanned(username);
    }

    public boolean banUser(String username){
        if(exists(username)){
            database.banUser(username);
            return true;
        }
        return false;
    }

    public boolean unbanUser(String username){
        if(exists(username)){
            database.unbanUser(username);
            return true;
        }
        return false;
    }

    public boolean isBanned(String username){
        return database.isBanned(username);
    }

    public ArrayList<Integer> getAllGroups(String username){
        ArrayList<Integer> result = new ArrayList<>();
        for(int groupID:database.getAllGroups())
            if(database.getAllUsers(groupID).contains(username))
                result.add(groupID);
        return result;
    }

    public Map<String, ArrayList<String>> getAllGroupsWithUsers(String username) {
        Map<String, ArrayList<String>> result = new HashMap<>();
        ArrayList<Integer> groups = getAllGroups(username);
        for(Integer groupID:groups){
            result.put(database.getName(groupID), database.getAllUsers(groupID));
        }
        return result;
    }


    /*
    public boolean createUser(String username, String password){
        if(!userExists(username)){

        }
        //Check if Username already exists
        //create Database entry
        return true;
    }

    public boolean loginUser(String username, String password){

        //Password und Username abgleichen
        String query = "";

        return true;
    }

    public boolean userExists(String username){
        //Username abgleichen
        String query = "";
        ResultSet rs = mySQLConnection.executeQuery(query);
        try {
            if(rs.next()){
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return false;
    }

    public void permaBanUser(String username){
        String query = "";

    }

    public void tempBanUser(String username, Date expiringDate){
        String query = "";

    }

    public void removeBan(String username){
        String query = "";

    }
    */
}
