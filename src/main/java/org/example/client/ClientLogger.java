package org.example.client;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientLogger {
    private static ClientLogger logger = new ClientLogger();

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

    public ClientLogger(){};

    public static ClientLogger getInstance() {
        if(logger == null) logger = new ClientLogger();
        return logger;
    }
}
