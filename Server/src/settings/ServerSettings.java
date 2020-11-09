package settings;

public class ServerSettings {

    private String ipAddress;
    private String port;
    private String database;
    private String databaseUser;
    private String databasePassword;

    public ServerSettings(String ipAddress, String port, String database, String databaseUser, String databasePassword) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.database = database;
        this.databaseUser = databaseUser;
        this.databasePassword = databasePassword;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }
}
