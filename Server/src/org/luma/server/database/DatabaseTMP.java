package org.luma.server.database;

import java.lang.reflect.Array;
import java.util.*;

public class DatabaseTMP {

    private final Map<String, String> user;
    private final Map<Integer, ArrayList<String>> groups;
    private final Set<String> bannedUser;
    private final Map<Integer, String> groupNames;

    public DatabaseTMP() {
        user = new HashMap<>();
        groups = new HashMap<>();
        bannedUser = new HashSet<>();
        groupNames = new HashMap<>();
    }

    public void addUser(String username, String password) {
        user.put(username, password);
    }

    public void removeUser(String username) {
        user.remove(username);
    }

    public boolean existsUser(String username) {
        return user.containsKey(username);
    }

    public boolean checkPassword(String username, String password) {
        return user.get(username).matches(password);
    }


    public void banUser(String username) {
        bannedUser.add(username);
    }

    public void unbanUser(String username) {
        bannedUser.remove(username);
    }

    public boolean isBanned(String username) {
        return bannedUser.contains(username);
    }


    public int addGroup(String name) {
        for(int i=0; i<groups.size(); i++){
            if(!groups.containsKey(i)) {
                groups.put(i, new ArrayList<String>());
                groupNames.put(i, name);
                return i;
            }
        }
        groupNames.put(groups.size(), name);
        groups.put(groups.size(), new ArrayList<String>());
        return groups.size() - 1;
    }

    public void removeGroup(Group group) {
        groups.remove(group.getId());
        groupNames.remove(group.getId());
    }

    public void addUserToGroup(Group group, String username){
        groups.get(group.getId()).add(username);
    }

    public void removeUserFromGroup(Group group, String username){
        groups.get(group.getId()).remove(username);
        System.out.println(groups.get(group.getId()));
    }

    public String getName(int groupID){
        return groupNames.get(groupID);
    }

    public ArrayList<String> getAllUsers(int id){
        return groups.get(id);
    }

    public Set<Integer> getAllGroups(){
        return groups.keySet();
    }

    public void changeGroupName(int id, String name) {
        groupNames.put(id, name);
        System.out.println(groupNames.get(id));
    }
}
