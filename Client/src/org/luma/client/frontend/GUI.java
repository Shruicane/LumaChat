package org.luma.client.frontend;

public interface GUI {
    void updateMessages(String type, String group, String s);
    void showPopup(String msg, String hint);
    void showPopup(String msg);

    void logout();

    void updateGroupView(Object information);

    void updatePrivateView(Object information);
}
