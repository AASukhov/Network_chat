import org.example.server.ServerLogger;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTest {

    int port;
    private static final ServerLogger logger = ServerLogger.getInstance();
    String path;
    String host;


    @Test
    public void testServerMain() {
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
        assertEquals(9999, port);
        assertEquals("127.0.0.1", host);
        assertEquals("serverLog.txt",path);
    }
}
