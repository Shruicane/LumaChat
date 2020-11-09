package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class UserManagement {

    private MySQLConnection mySQLConnection;

    public UserManagement(MySQLConnection mySQLConnection) {
        this.mySQLConnection = mySQLConnection;
    }

    public boolean createUser(String username, String password){
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



}
