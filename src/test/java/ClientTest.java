import org.example.client.ClientLogger;
import org.junit.jupiter.api.Test;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientTest {
    private static final ClientLogger logger = ClientLogger.getInstance();
    String path = "clientLog.txt";
    @Test
    public void testClientMain() {
        String msg = "Test message";
        File file = new File(path);
        long beforeLength = file.length();
        logger.log(msg,path);
        long afterLength = file.length();
        boolean afterLengthOverBefore = afterLength > beforeLength;
        assertTrue(afterLengthOverBefore);
    }
}
