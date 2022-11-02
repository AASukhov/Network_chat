package org.example.server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerLogger {
    private static ServerLogger logger = new ServerLogger();

    public void log (String message, String path) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(path, true));
            writer.write("[" + LocalDateTime.now().
                    format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) + "] " + message +"\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ServerLogger getInstance() {
        if(logger == null) logger = new ServerLogger();
        return logger;
    }
}
