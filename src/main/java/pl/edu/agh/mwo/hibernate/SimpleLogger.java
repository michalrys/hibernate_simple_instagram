package pl.edu.agh.mwo.hibernate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimpleLogger {
    public final static File FILE_WITH_LOGS = new File("./src/main/resources/logs.txt");
    private static StringBuilder messages = new StringBuilder();

    public void write(String message) {
        createLogFileIfNotExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_WITH_LOGS, true))) {
            String head = "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd_HH-mm-ss")) + "]: ";
            String tail = "--------------------------------------\n";
            writer.write(head + message + tail);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createLogFileIfNotExists() {
        if (!FILE_WITH_LOGS.exists()) {
            try {
                FILE_WITH_LOGS.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void append(String message) {
        messages.append(message);
    }

    public void writeStoredMassages() {
        if (messages.toString() == "") {
            return;
        }
        write(messages.toString());
        messages = new StringBuilder();
    }

    public void clearFile() {
        FILE_WITH_LOGS.delete();
    }
}
