import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
public class Main1 {
    private static final Logger logger = Logger.getLogger(Main1.class.getName());
    private static final String OK_RESPONSE = "HTTP/1.1 200 OK\r\n\r\n";
    private static final String NOT_FOUND_RESPONSE = "HTTP/1.1 404 Not Found\r\n\r\n";

    public static void main(String[] args) {
        logger.info("Logs will appear here!");

        try {
            // ServerSocket creates a server that listens for incoming TCP connections on port 4221.
            // This socket does NOT send or receive data directly. it only accepts new client connections.
            ServerSocket serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);

            Socket clientSocket = serverSocket.accept(); // Blocking call, waits for a client connection.
            logger.info("accepted new connection");

            // Handle request and send appropriate HTTP response.
            sendResponse(clientSocket);

            clientSocket.close();   // Close the connection with the client.
            serverSocket.close();   // Close the server socket.
            logger.info("closed connection");
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    private static void sendResponse(Socket acceptSocket) throws IOException {
        String response = OK_RESPONSE;

        // getInputStream gives raw bytes whereas InputStreamReader converts bytes to characters. (default UTF-8 encoding)
        // BufferedReader reads text from a character-input stream, buffering characters for efficient reading.
        try (var reader = new BufferedReader(new InputStreamReader(acceptSocket.getInputStream()))) {
            String requestLine = reader.readLine(); // Read only the first line (e.g., GET /path HTTP/1.1)

            if (requestLine != null) {
                String[] tokens = requestLine.split("\\s+"); // Split by one or more whitespace characters

                // Check for malformed request or unsupported path
                if (tokens.length < 3 || 
                    (!"/index.html".equals(tokens[1]) && !"/".equals(tokens[1]))) {
                    response = NOT_FOUND_RESPONSE;
                }

                System.out.println("Request: " + requestLine);
            }

            // OutputStream is used to send data to the client.
            // It is obtained from the Socket object.
            // The OutputStream is buffered, meaning it collects data before sending it over the network.
            // This is why we need to call flush() to ensure the data is sent immediately.
            OutputStream out = acceptSocket.getOutputStream();
            out.write(response.getBytes(StandardCharsets.UTF_8)); // Write response bytes to buffer
            out.flush(); // Send the buffer’s contents over the network
        }
    }
}
