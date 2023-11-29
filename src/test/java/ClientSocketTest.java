import edu.hillel.ClientSc;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

public class ClientSocketTest {
    @Test
    public void testSendMessage() throws IOException {
        OutputStream outputStream = mock(OutputStream.class);
        PrintWriter printWriter = mock(PrintWriter.class);

        ClientSc client = new ClientSc();

        String scannerMessage = "file file.txt";
        Scanner scanner = new Scanner(scannerMessage);
        client.sendMessage(scanner, printWriter, outputStream);

        verify(printWriter, atLeastOnce()).println(scannerMessage);

        verify(outputStream, atLeastOnce()).write(any(byte[].class), anyInt(), anyInt());
        verify(outputStream, atLeastOnce()).flush();
    }

    @Test
    public void testSendMessageToAllClint() throws IOException {
        BufferedReader bufferedReader = mock(BufferedReader.class);

        ClientSc socketClient = new ClientSc();
        socketClient.sendMessageToAllClients(bufferedReader);
    }

    @Test
    public void testSendFile() throws IOException {
        OutputStream outputStream = mock(OutputStream.class);
        String msg = "file file.txt";

        ClientSc socketClient = new ClientSc();

        String errorSendFileMessage = socketClient.sendFile(msg, outputStream);

        Assert.assertNull(errorSendFileMessage);

        verify(outputStream, atLeastOnce()).write(any(byte[].class), anyInt(), anyInt());
        verify(outputStream, atLeastOnce()).flush();
    }
}
