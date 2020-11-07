package Objects;

import java.io.Serializable;

public class Login implements Serializable {
    private String name;
    private String password;

    public Login(String name, String password){
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Login{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
