import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final String NOT_FOUND_RESPONSE = "HTTP/1.1 404 Not Found\r\n\r\n";
    public static Map<String, String> map = new HashMap<>();
    public static void main(String[] args) {

        try {
            for (int i = 0; i< args.length; i++){
                if (args[i].equals("--directory")) {
                    map.put("dir", args[i+1]);
                }
            }
        } catch (Exception e){
            System.out.println("args error: " + e.getMessage());
        }

        try (ServerSocket serverSocket = new ServerSocket(4221)) {
            // ServerSocket creates a server that listens for incoming TCP connections on
            // port 4221
            // This socket does NOT send or receive data directly. it only accepts new
            // client connections.
            serverSocket.setReuseAddress(true);
            while (true) {
                Socket clientSocket = serverSocket.accept(); // blocking call
                logger.info("Accepted new connection");

                // Handle client in a new thread
                new Thread(() -> {
                    try {
                        sendResponse(clientSocket);
                    } catch (IOException e) {
                        logger.severe("Error handling client: " + e.getMessage());
                    } 
                    finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            logger.severe("Error closing client socket: " + e.getMessage());
                        }
                    }
                }).start();
            }

        } catch (IOException e) {
            logger.severe("IOException: " + e.getMessage());
        }
    }

private static void sendResponse(Socket clientSocket) throws IOException {
    try (
        var reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        var writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
    ) {
        while (true) {
            Request request;
            try {
                request = new Request(reader);
                if (request.getrequestLine() == null) {
                    // close client connection
                    break;
                }
            } catch (IOException e) {
                break;
            }

            RequestHandler reqHandler = new RequestHandler(request, writer, clientSocket.getOutputStream());
            reqHandler.handle();
            System.out.println("Request: " + request.getrequestLine());

            String connectionHeader = request.getHeader("connection");
            if (connectionHeader != null && connectionHeader.equalsIgnoreCase("close")) {
                break;
            }
        }
    } finally {
        clientSocket.close();
    }
}


}
