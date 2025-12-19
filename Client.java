import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws Exception {
        BufferedReader keyboard = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.print("Enter your username: ");
        String username = keyboard.readLine();
        if (username == null || username.trim().isEmpty()) {
            username = "Guest";
        }

        Socket socket = new Socket("localhost", 1234);
        System.out.println("Connected to chat server");
        System.out.println("Type messages and press Enter. Type /exit to quit.");
        System.out.println();

        BufferedReader input = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Send username to server
        out.println(username);

        // Thread to receive messages
        new Thread(() -> {
            try {
                String msg;
                while ((msg = input.readLine()) != null) {
                    System.out.println(msg);
                }
            } catch (IOException e) {}
            System.exit(0);
        }).start();

        // Main thread to send messages
        String message;
        while ((message = keyboard.readLine()) != null) {
            out.println(message);
            if (message.equalsIgnoreCase("/exit")) {
                System.out.println("Disconnecting...");
                break;
            }
        }
        socket.close();
    }
}
