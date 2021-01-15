package org.luma.server.database;

import org.luma.server.frontend.controller.Controller;

import java.util.ArrayList;

public class GroupManagement {

    private MySQLConnection mySQLConnection;
    private Controller controller;

    private DatabaseTMP database;

    public GroupManagement(MySQLConnection mySQLConnection, Controller controller) {
        this.mySQLConnection = mySQLConnection;
        this.controller = controller;
        this.database = mySQLConnection.getDatabase();
    }

    public int createGroup(String name){
        return database.addGroup(name);
    }

    public void deleteGroup(Group group){
        database.removeGroup(group);
    }

    public void addUser(Group group, String username){
        database.addUserToGroup(group, username);
    }

    public void removeUser(Group group, String username){
        database.removeUserFromGroup(group, username);
    }

    public ArrayList<String> getAllUsers(int groupID){
        return database.getAllUsers(groupID);
    }

    public void changeName(int id, String name) {
        database.changeGroupName(id, name);
    }

    public String getName(int groupID){
        return database.getName(groupID);
    }

    public int getID(String name){
        return database.getID(name);
    }

    public DatabaseTMP getDatabase() {
        return database;
    }

    /*public void createGroup(int groupID){

    }

    public void deleteGroup(int groupID){

    }

    public void addUserToGroup(int groupID, String userName){

    }

    public void removeUserFromGroup(int groupID, String userName){

    }*/
}
