package org.luma.server.settings;

import org.luma.server.database.MySQLConnection;

public class ServerMain {
    public static void main(String[] args) {
        MySQLConnection mySQLConnection = new MySQLConnection("127.0.0.1", "3306", "root", "", "test");
    }
}
