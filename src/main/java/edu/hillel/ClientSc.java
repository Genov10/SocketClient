package edu.hillel;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.file.Files;
import java.util.Scanner;

public class ClientSc {
    private static final  String HOST = "localhost";
    private static final  int PORT = 8080;

    public void start() throws IOException {
        try (Socket clientSocket = new Socket(HOST, PORT)) {
            try (PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                new Thread(() -> {
                    try {
                        sendMessageToAllClients(bufferedReader);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                Scanner scanner = new Scanner(System.in);
                sendMessage(scanner, printWriter, clientSocket.getOutputStream());

            }
        }
    }

    public void sendMessageToAllClients(BufferedReader bufferedReader) throws IOException {
        try {
            String message;
            while ((message = bufferedReader.readLine()) != null) {
                System.out.println(message);
            }
        } catch (IOException e) {
            System.out.println(" " + e);
        }
    }

    public void sendMessage(Scanner scanner, PrintWriter printWriter, OutputStream outputStream) throws IOException {
        while (scanner.hasNextLine()) {
            String message = scanner.nextLine();
            printWriter.println(message);
            if (message.equals("end")){
                break;
            } else if (message.startsWith("file ")) {
                String sendFileError = sendFile(message, outputStream);
                if (sendFileError != null) {
                    System.out.println("Error: " + sendFileError);
                }
            }
        }
    }

    public String sendFile(String message, OutputStream outputStream) throws IOException{
        String fileName = (message.substring(message.lastIndexOf(' ') + 1)).trim();
        if (fileName.isEmpty()) {
            return "File name is empty";
        }
        File file = new File(fileName);
        if (!file.exists()) {
            return "File not exists";
        }

        int length;
        byte[] bufferedBytes = new byte[8000];
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
            while ((length = bufferedInputStream.read(bufferedBytes)) >= 0) {
                outputStream.write(bufferedBytes, 0, length);
                outputStream.flush();
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        ClientSc clientSc = new ClientSc();
        clientSc.start();
    }

}
