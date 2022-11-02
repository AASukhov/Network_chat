package org.example.client;

import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class Client {
    private int port;
    private String host;
    private final ClientLogger logger;
    private BufferedReader in;
    private PrintWriter out;
    private String path;
    private Socket socket;
    private Scanner scanner;

    public Client() {
        logger = ClientLogger.getInstance();
        try (FileReader reader = new FileReader("settings.txt")) {
            Properties properties = new Properties();
            properties.load(reader);
            port = Integer.parseInt(properties.getProperty("SERVER_PORT"));
            host = properties.getProperty("SERVER_HOST");
            path = properties.getProperty("CLIENT_LOG");
        }   catch (IOException e) {
                logger.log("Problem with FileReading",path);
                e.printStackTrace();
            }

        try {
            this.socket = new Socket(host, port);
        } catch (IOException e) {
            logger.log("Problem with the connection",path);
            e.printStackTrace();
        }

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            scanner = new Scanner(System.in);
            System.out.print("Введите Ваше имя: ");
            String name = scanner.nextLine();
            out.println("/name " + name);
            String receivedMessage = "SERVER: " + in.readLine();
            System.out.println(receivedMessage);
            logger.log(receivedMessage, path);
        } catch (IOException e) {
            logger.log("Problem with messages transmission",path);
            e.printStackTrace();
        }
        new Write().start();
        new Read().start();
    }

    public static void main(String[] args) {
        new Client();
    }

    private class Read extends Thread {
        @Override
        public void run() {
            String message;
            try  {
                while (true) {
                    System.out.print(">: ");
                    message = in.readLine();
                    if (message.equals("/exit")) {
                        logger.log(message, path);
                        break;
                    }
                    System.out.println(message);
                    logger.log(message, path);
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.log("Reading chat problem", path);
            }
        }
    }

    public class Write extends Thread {
        @Override
        public void run() {
            while (true) {
                String userMsg;
                userMsg = scanner.nextLine();
                if (userMsg.equals("/exit")) {
                    logger.log(" has left chat", path);
                    //out.println("user has left chat");
                    break;
                }
                else {
                    out.println(userMsg);
                }
                out.flush();
            }
        }
    }
}
