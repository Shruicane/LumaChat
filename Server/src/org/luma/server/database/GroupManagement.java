package org.luma.server.database;

import org.luma.server.frontend.controller.Controller;

public class GroupManagement {

    private MySQLConnection mySQLConnection;
    private Controller controller;

    private DatabaseTMP database;

    public GroupManagement(MySQLConnection mySQLConnection, Controller controller) {
        this.mySQLConnection = mySQLConnection;
        this.controller = controller;
        this.database = mySQLConnection.getDatabase();
    }

    public int createGroup(){
        return database.addGroup();
    }

    public void deleteGroup(Group group){
        database.removeGroup(group);
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
