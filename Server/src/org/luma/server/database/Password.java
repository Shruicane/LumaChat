package org.luma.server.database;

public class Password {
    private String password;
    private boolean visible;

    public Password(String password, boolean visible){
        this.password = password;
        this.visible = visible;
    }

    public void show(boolean state){
        visible = state;
    }

    @Override
    public String toString() {
        return visible ? password : "*****";
    }
}
