import java.io.*;
import java.net.*;
import java.util.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Server {
    private static Set<ClientHandler> clients = new HashSet<>();
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("Server started... Waiting for clients");

        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler handler = new ClientHandler(socket);
            synchronized (clients) {
                clients.add(handler);
            }
            handler.start();
        }
    }

    static class ClientHandler extends Thread {
        Socket socket;
        PrintWriter out;
        BufferedReader in;
        String username = null;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                username = in.readLine();
                if (username == null || username.trim().isEmpty()) {
                    username = "Guest";
                }

                String timestamp = LocalTime.now().format(timeFormatter);
                broadcastMessage("[" + timestamp + "] " + username + " joined the chat", null);
                System.out.println(username + " connected");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("/exit")) {
                        break;
                    }
                    timestamp = LocalTime.now().format(timeFormatter);
                    broadcastMessage("[" + timestamp + "] " + username + ": " + message, null);
                }
            } catch (Exception e) {
                System.out.println("Client error: " + e.getMessage());
            } finally {
                synchronized (clients) {
                    clients.remove(this);
                }
                String timestamp = LocalTime.now().format(timeFormatter);
                broadcastMessage("[" + timestamp + "] " + username + " left the chat", null);
                System.out.println(username + " disconnected");
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }
    }

    private static void broadcastMessage(String message, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (sender == null || !client.equals(sender)) {
                    client.out.println(message);
                }
            }
        }
        System.out.println(message);
    }
}
