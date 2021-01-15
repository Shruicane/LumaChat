package org.luma.server.database;


import org.luma.server.frontend.controller.Controller;
import org.luma.server.settings.Settings;

import java.sql.*;

public class MySQLConnection {

    private UserManagement userManager;
    private GroupManagement groupManager;

    public MySQLConnection(Controller controller){
        userManager = new UserManagement(this, controller,
                new MySQLDataBase(Settings.getIpAddress(), Settings.getPort(), Settings.getDatabaseUser(), Settings.getDatabasePassword(), Settings.getDatabase()));
        groupManager = new GroupManagement(this, controller,
                new MySQLDataBase(Settings.getIpAddress(), Settings.getPort(), Settings.getDatabaseUser(), Settings.getDatabasePassword(), Settings.getDatabase()));
    }


    public UserManagement getUserManager() {
        return userManager;
    }

    public GroupManagement getGroupManager() {
        return groupManager;
    }

    /*

    private Connection conn;
    private String JDBC_DRIVER;
    private final String DB_URL;
    private String user;
    private String password;
    private String database;

    public MySQLConnection(String IP, String port, String user, String password, String database) {
        this.JDBC_DRIVER = "com.mysql.jdbc.Driver";
        this.DB_URL = "jdbc:mysql://" + IP + ":" + port + "/" + database;
        this.user = user;
        this.password = password;
        this.database = database;
        //Einmaliges Initialisieren der Datenbank
        this.initDatabase();
    }

    private void openConnection()  {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.conn = DriverManager.getConnection(this.DB_URL, this.user, this.password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection(){
        try {
            this.conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query){
        ResultSet rs = null;
        try {
            Statement stmt = this.conn.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rs;
    }

    private void initDatabase(){
        if(! this.tablesExist()){
            this.createTables();
        }
    }

    private boolean tablesExist(){
        this.openConnection();
        String[] tables = {"userdata", "groupdata", "messages", "logdata", "uuidlist"};

        for(String table : tables){
            String queryAccountTable = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '"
                    + this.database + "' AND TABLE_NAME = '" + table + "'";
            try {
                ResultSet rs = this.executeQuery(queryAccountTable);
                System.out.println(rs.next());
                if(rs.getInt(1) != 1){
                    return false;
                }
            } catch (SQLException throwables) {
                //Fehler aufgetreten, bedeutet die Datenbank liegt nicht in der erwarteten Form vor
                throwables.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private void createTables(){
        String userDataQuery = "DROP TABLE IF EXISTS `userdata`;CREATE TABLE `userdata` ( `ID` int(11) NOT NULL, `UUID` varchar(36) NOT NULL, `Username` " +
                "varchar(30) NOT NULL, `Password` varchar(30) NOT NULL, `Chatlist` text NOT NULL, " +
                "`BanStatus` int(11) NOT NULL, `BanExpiry` date NOT NULL ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
        String groupDataQuery = "DROP TABLE IF EXISTS `groupdata`;CREATE TABLE `groupdata` ( `ID` int(11) NOT NULL, `GroupUUID` varchar(36) NOT NULL, " +
                "`Userlist` text NOT NULL, `Type` int(11) NOT NULL, `Name` varchar(30) NOT NULL ) " +
                "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
        String messagesQuery = "DROP TABLE IF EXISTS `messages`;CREATE TABLE `messages` ( `ID` int(11) NOT NULL, `GroupUUID` varchar(36) NOT NULL, " +
                "`Message` text NOT NULL, `Date` varchar(10) NOT NULL, `Time` varchar(5) NOT NULL, " +
                "`Sender` varchar(36) NOT NULL ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
        String logDataQuery = "DROP TABLE IF EXISTS `logdata`;CREATE TABLE `logdata` ( `ID` int(11) NOT NULL, `LogMessage` text NOT NULL, " +
                "`LogLvl` int(11) NOT NULL, `Datum` varchar(10) NOT NULL, `Uhrzeit` varchar(5) NOT NULL ) " +
                "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
        String uuidListQuery = "DROP TABLE IF EXISTS `uuidlist`;CREATE TABLE `uuidlist` ( `ID` int(11) NOT NULL, `UUID` varchar(36) NOT NULL ) " +
                "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; ";
        this.executeQuery(userDataQuery);
        this.executeQuery(groupDataQuery);
        this.executeQuery(messagesQuery);
        this.executeQuery(logDataQuery);
        this.executeQuery(uuidListQuery);
    }*/
}