import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {

    private static String OK_RESPONSE = "HTTP/1.1 200 OK\r\n\r\n";
    private static String NOT_FOUND_RESPONSE = "HTTP/1.1 404 Not Found\r\n\r\n";

    public static void main(String[] args) throws IOException {
        // You can use print statements as follows for debugging, they'll be visible when running
        // tests.
        System.out.println("Logs from your program will appear here!");

        try (ServerSocket serverSocket = new ServerSocket(4221)) {

            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);

            Socket socket = serverSocket.accept(); // Wait for connection from client.

            sendResponse(socket);

            System.out.println("accepted new connection");
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    private static void sendResponse(Socket acceptSocket) throws IOException {

        var requestStream = acceptSocket.getInputStream();
        String response = OK_RESPONSE;

        // handle request
        try (var inStrReader = new InputStreamReader(requestStream);
                BufferedReader reader = new BufferedReader(inStrReader)) {
            int lineNo = 1;
            while (reader.ready()) {

                String line = reader.readLine();

                if (lineNo == 1) {

                    String[] tokens = line.split("\\s+");
                    if (tokens.length < 3
                            || (!"/index.html".equals(tokens[1]) && !"/".equals(tokens[1]))) {
                        response = NOT_FOUND_RESPONSE;
                    }
                }

                lineNo++;
            }

            acceptSocket.getOutputStream().write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
}