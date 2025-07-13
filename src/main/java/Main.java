import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final String OK_RESPONSE = "HTTP/1.1 200 OK\r\n\r\n";
    private static final String NOT_FOUND_RESPONSE = "HTTP/1.1 404 Not Found\r\n\r\n";

    public static void main(String[] args) {
        logger.info("Logs will appear here!");

        try {
            // ServerSocket creates a server that listens for incoming TCP connections on port 4221
            // This socket does NOT send or receive data directly. it only accepts new
            // client connections.
            ServerSocket serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);

            Socket clientSocket = serverSocket.accept(); // Blocking call, waits for a client connection.
            logger.info("accepted new connection");

            // Handle request and send appropriate HTTP response.
            sendResponse(clientSocket);

            clientSocket.close(); // Close the connection with the client.
            serverSocket.close(); // Close the server socket.
            logger.info("closed connection");
        } catch (IOException e) {
            logger.severe("IOException: " + e.getMessage());
        }
    }

    private static void sendResponse(Socket acceptSocket) throws IOException {
        String response = NOT_FOUND_RESPONSE;

        // getInputStream gives raw bytes whereas InputStreamReader converts bytes to
        // characters. (default UTF-8 encoding)
        // BufferedReader reads text from a character-input stream, buffering characters
        // for efficient reading.
        try (var reader = new BufferedReader(new InputStreamReader(acceptSocket.getInputStream()));
             var writer = new BufferedWriter(new OutputStreamWriter(acceptSocket.getOutputStream()))) {

            String requestLine = reader.readLine(); // Read only the first line (e.g., GET /path HTTP/1.1)

            if (requestLine != null) {
                String[] tokens = requestLine.split("\\s+"); // Split by one or more whitespace characters
                String method = tokens[0]; // HTTP method (e.g. GET)
                String path = tokens[1]; // Requested path (e.g. /index.html)

                // Check for malformed request or unsupported path
                if (method.equals("GET")) {
                    if (path.equals("/")) {
                        response = OK_RESPONSE;
                        writer.write(response);
                    } else if (path.split("/")[1].equals("echo")) {
                        String echo = path.split("/")[2]; // first is empty string, second is "echo", third is payload
                        String fullResponse = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: "
                                + echo.length() + "\r\n\r\n" + echo;
                        writer.write(fullResponse);
                    } else if (path.split("/")[1].equals("index.html")) {
                        response = OK_RESPONSE;
                        writer.write(response);
                    } else {
                        writer.write(response);
                    }
                }

                writer.flush(); // Ensure all data is sent
                System.out.println("Request: " + requestLine);
            }
        }
    }
}
