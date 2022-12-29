package net.earomc.earonick.config;

import net.earomc.earonick.EaroNick;
import org.jetbrains.annotations.Nullable;

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

    public FileWrapper(EaroNick plugin, File path, String name) {
        try {
            file = new File(path, name);
            if (file.createNewFile()) {
                plugin.getLogger().log(Level.INFO, "File created successfully");
            } else {
                plugin.getLogger().log(Level.INFO, "File already exist!");
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Cannot create File!");
            e.printStackTrace();
        }
    }

    @Nullable
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