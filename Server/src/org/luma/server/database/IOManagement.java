package org.luma.server.database;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class IOManagement {

    public IOManagement() {
    }

    public void writeLog(String path, String log) {
        if (!fileExists(path))
            createFile(path);
        write(log, path, true);
    }

    public void write(String content, String path, boolean oldContentStay) {
        try {
            if (oldContentStay) {
                FileWriter writer = new FileWriter(path, true);
                writer.write(content + "\n");
                writer.close();
            } else {
                PrintWriter writer = new PrintWriter(path, "UTF-8");
                writer.print(content);
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean fileExists(String path) {
        return new File(path).exists();
    }

    public void createFile(String path) {
        try {
            new File(new File(path).getParent()).mkdirs();
            new File(path).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
