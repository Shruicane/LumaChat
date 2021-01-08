package Objects;

public class Register extends RequestObject {
    public Register(String name, String password){
        super("Register", name, password);
    }
}