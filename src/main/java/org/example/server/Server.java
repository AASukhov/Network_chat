package org.example.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;


public class Server implements Runnable{
    private static int port;
    private static String host;
    private static String path;
    private static final ServerLogger logger = ServerLogger.getInstance();
    static Socket socket;
    public static ArrayList<Socket> clients = new ArrayList<>();


    public static void main (String[] args) {
        Server.Connect();
    }
    public Server(Socket socket) {
        Server.socket = socket;
    }

    @Override
    public void run() {
        String userName = "";

        logger.log("Сервер запущен. Port: " + port + " Host: " + host, path);
        System.out.println("Сервер запущен. Port: " + port + " Host: " + host);

        while (true) {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.lastIndexOf("/name") == 0) {
                        userName = line;
                        userName = userName.replace("/name", "").trim();
                        logger.log(userName + " присоединился к чату", path);
                        send("Привет, " + userName + "!");
                        continue;
                    }
                    send('[' + userName + "] " + line);
                    if (line.equals("/exit")) {
                        logger.log(userName + " вышел из чата", path);
                        send(userName + " вышел из чата");
                        clients.remove(socket);
                        break;
                    }
                }

            } catch (IOException ex) {
                logger.log(ex.getMessage(), path);
                ex.printStackTrace(System.out);
            }
        }
    }

    private void send(String message) throws IOException {
        for (Socket client : clients) {
            if (client.isClosed()) continue;
            PrintWriter sender = new PrintWriter(client.getOutputStream());
            sender.println(message);
            sender.flush();
        }
        logger.log(message,path);
    }

    public static void Connect() {
        try (FileReader reader = new FileReader("settings.txt")) {
            Properties props = new Properties();
            props.load(reader);
            port = Integer.parseInt(props.getProperty("SERVER_PORT"));
            host = props.getProperty("SERVER_HOST");
            path = props.getProperty("SERVER_LOG");
        } catch (IOException ex) {
            logger.log(ex.getMessage(), path);
            System.out.println(ex.getMessage());
        }

        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                Socket client = server.accept();
                clients.add(client);
                new Thread(new Server(client)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(e.getMessage(), path);
        }
    }
}
