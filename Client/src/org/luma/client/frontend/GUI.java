package org.luma.client.frontend;

public interface GUI {
    void updateMessages(String s);
    void showPopup(String msg, String hint);
    void showPopup(String msg);

    void logout();
}
