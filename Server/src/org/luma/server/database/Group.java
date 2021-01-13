package org.luma.server.database;

public class Group {

    private final int id;

    public Group(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Group " + (id+1);
    }
}
