package org.luma.server.database;

import java.lang.reflect.Array;
import java.util.*;

public class DatabaseTMP {

    private final Map<String, String> user;
    private final Map<Integer, ArrayList<String>> groups;
    private final Set<String> bannedUser;

    public DatabaseTMP() {
        user = new HashMap<>();
        groups = new HashMap<>();
        bannedUser = new HashSet<>();
    }

    public void addUser(String username, String password) {
        user.put(username, password);
    }

    public void deleteUser(String username) {
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


    public int addGroup() {
        for(int i=0; i<groups.size(); i++){
            if(!groups.containsKey(i)) {
                groups.put(i, new ArrayList<String>());
                return i;
            }
        }
        groups.put(groups.size(), new ArrayList<String>());
        return groups.size() - 1;
    }

    public void removeGroup(Group group) {
        groups.remove(group.getId());
    }
}
