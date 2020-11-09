package org.luma.server.settings;

import java.util.prefs.Preferences;

public class Settings {

    private static String ipAddress;
    private static String port;
    private static String database;
    private static String databaseUser;
    private static String databasePassword;

    private static final String defaultIpAddress = "127.0.0.1";
    private static final String defaultPort = "3306";
    private static final String defaultDatabase = "lumauserdatabase";
    private static final String defaultDatabaseUser = "root";
    private static final String defaultDatabasePassword = "";

    private static final String ipAddressKey = "databaseIP";
    private static final String portKey = "databasePort";
    private static final String databaseKey = "databasename";
    private static final String databaseUserKey = "databaseUser";
    private static final String databasePasswordKey = "databasePass";

    private static final Preferences prefs =  Preferences.userRoot().node(Settings.class.getName());

    public static void loadSettings(){
        ipAddress = prefs.get(ipAddressKey, defaultIpAddress);
        port = prefs.get(portKey, defaultPort);
        database = prefs.get(databaseKey, defaultDatabase);
        databaseUser = prefs.get(databaseUserKey, defaultDatabaseUser);
        databasePassword = prefs.get(databasePasswordKey, defaultDatabasePassword);
    }

    public static void saveSettings(ServerSettings serverSettings){
        prefs.put(ipAddressKey, serverSettings.getIpAddress());
        prefs.put(portKey, serverSettings.getPort());
        prefs.put(databaseKey, serverSettings.getDatabase());
        prefs.put(databaseUserKey, serverSettings.getDatabaseUser());
        prefs.put(databasePasswordKey, serverSettings.getDatabasePassword());
        loadSettings();
    }

    public static String getIpAddress() {
        loadSettings();
        return ipAddress;
    }

    public static String getPort() {
        loadSettings();
        return port;
    }

    public static String getDatabase() {
        loadSettings();
        return database;
    }

    public static String getDatabaseUser() {
        loadSettings();
        return databaseUser;
    }

    public static String getDatabasePassword() {
        loadSettings();
        return databasePassword;
    }

    public static String getDefaultIpAddress() {
        loadSettings();
        return defaultIpAddress;
    }

    public static String getDefaultPort() {
        loadSettings();
        return defaultPort;
    }

    public static String getDefaultDatabase() {
        loadSettings();
        return defaultDatabase;
    }

    public static String getDefaultDatabaseUser() {
        loadSettings();
        return defaultDatabaseUser;
    }

    public static String getDefaultDatabasePassword() {
        loadSettings();
        return defaultDatabasePassword;
    }

}
