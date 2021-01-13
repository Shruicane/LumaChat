package org.luma.server.database;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class User {

    private String username;
    private String password;
    private boolean online;

    private final SimpleStringProperty usernameProperty;
    private final SimpleObjectProperty<Password> passwordProperty;

    public User(String username, String password, boolean online, boolean visible){
        this.username = username;
        this.password = password;
        this.online = online;
        this.usernameProperty = new SimpleStringProperty(username);
        this.passwordProperty = new SimpleObjectProperty<>(new Password(password, visible));
    }

    public String getName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isOnline() {
        return online;
    }

    public SimpleStringProperty usernameProperty() {
        return usernameProperty;
    }

    public SimpleObjectProperty<Password> passwordProperty() {
        return passwordProperty;
    }
}
