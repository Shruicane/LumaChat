package org.luma.server.database;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class IOManagement {


    private final String logPath = "logs/";
    private final String dataPath = "data/";

    public IOManagement() {
        if (!fileExists(logPath + "latest.txt"))
            createFile(logPath + "latest.txt");
        else {
            String date = getLastModified(logPath + "latest.txt");
            renameFile(logPath + "latest.txt", logPath + date + "-" + (countDateFiles(date) + 1) + ".txt");
        }
    }







    public void writeLog(String log) {
        if (!fileExists(logPath + "latest.txt"))
            createFile(logPath + "latest.txt");
        write(log, logPath + "latest.txt", true);
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

    public void write(ArrayList<String> content, String path) {
        try {
            PrintWriter writer = new PrintWriter(path, "UTF-8");
            for (String line : content)
                writer.print(line + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> read(String path) {
        if(!fileExists(path))
            createFile(path);
        ArrayList<String> result = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.add(result.size(), line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private String getLastModified(String path) {
        File file = new File(path);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(file.lastModified());
    }

    private int countDateFiles(String date) {
        ArrayList<String> files = listFolderContentFILE(logPath);
        System.out.println(files);
        System.out.println(date);
        int count = 0;
        for (String file : files) {
            if (file.contains(date))
                count++;
        }
        System.out.println(count);
        return count;
    }

    private boolean fileExists(String path) {
        return new File(path).exists();
    }

    public void renameFile(String src, String dest) {
        new File(src).renameTo(new File(dest));
    }

    public void createFile(String path) {
        try {
            new File(new File(path).getParent()).mkdirs();
            new File(path).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> listFolderContentFILE(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> result = new ArrayList<>();

        for (File file : listOfFiles) {
            if (!file.isDirectory())
                result.add(file.getName());
        }

        return result;
    }
}
