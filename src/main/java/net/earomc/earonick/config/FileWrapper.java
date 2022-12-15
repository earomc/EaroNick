package net.earomc.earonick.config;

import net.earomc.earonick.EaroNick;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * @author tiiita_
 * Created on Dezember 07, 2022 | 16:33:48
 * (●'◡'●)
 */
public class FileWrapper {


    private File file;
    private final EaroNick plugin;

    public FileWrapper(EaroNick plugin, File path, String name) {
        this.plugin = plugin;

        try {
            file = new File(path, name);
            if (file.createNewFile()) {
                plugin.getLogger().log(Level.INFO, "Log file created successfully!");
            } else {
                plugin.getLogger().log(Level.INFO, "Log file already exist!");
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Cannot create log file!");
            e.printStackTrace();
        }
    }

    public void write(String content) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true))) {

            bufferedWriter.newLine();
            bufferedWriter.append(content);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String> getLines() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))){

            ArrayList<String> lines = new ArrayList<>();

            String lastLine;
            while ((lastLine = bufferedReader.readLine()) != null) {
                lines.add(lastLine);
            }

            return lines;


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}