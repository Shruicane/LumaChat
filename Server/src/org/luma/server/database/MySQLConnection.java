package org.luma.server.database;


import org.luma.server.frontend.controller.Controller;
import org.luma.server.settings.Settings;

import java.sql.*;

public class MySQLConnection {

    private final UserManagement userManager;
    private final GroupManagement groupManager;
    private final MessageManager messageManager;

    public MySQLConnection(Controller controller){
        MySQLDataBase mysqlDatabase = new MySQLDataBase(Settings.getIpAddress(), Settings.getPort(), Settings.getDatabaseUser(), Settings.getDatabasePassword(), Settings.getDatabase());
        userManager = new UserManagement(this, controller, mysqlDatabase);
        groupManager = new GroupManagement(this, controller, mysqlDatabase);
        messageManager = new MessageManager(mysqlDatabase);
    }


    public UserManagement getUserManager() {
        return userManager;
    }

    public GroupManagement getGroupManager() {
        return groupManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }
}