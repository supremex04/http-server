import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class RequestHandler {
    private final Request request;
    private final BufferedWriter writer;

    public RequestHandler(Request request, BufferedWriter writer) {
        this.request = request;
        this.writer = writer;
    }

    private void sendResponse(String response) throws IOException {
        writer.write(response);
        writer.flush();
    }

    public void handle() throws IOException {
        String method = request.getMethod();
        String[] path = request.getPath().split("/");
        if (method.equals("GET")) {
            if (request.getPath().equals("/")) {
                sendResponse("HTTP/1.1 200 OK\r\n\r\n");
            } else if (path[1].equals("echo")) {
                String echo = path[2]; // first is empty string, second is "echo", third is payload
                String fullResponse = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: "
                        + echo.length() + "\r\n\r\n" + echo;
                sendResponse(fullResponse);
            } else if (path[1].equals("user-agent")) {
                String userAgent = request.getHeader("user-agent");
                String fullResponse = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: "
                        + userAgent.length() + "\r\n\r\n" + userAgent;
                sendResponse(fullResponse);

            } else if (path[1].equals("files")) {
                String filePath = Main.map.get("dir") + path[2];
                File file = new File(filePath);
                if (file.exists()) {
                    byte[] fileBytes = Files.readAllBytes(file.toPath());
                    String fullResponse = "HTTP/1.1 200 OK\r\nContent-Type: application/octet-stream\r\nContent-Length: "
                            + fileBytes.length
                            + "\r\n\r\n"
                            + new String(fileBytes);
                    sendResponse(fullResponse);
                }
                else {
                    send404();
                }

            }

            else if (path[1].equals("index.html")) {
                sendResponse("HTTP/1.1 200 OK\r\n\r\n");
            } else {
                send404();
            }
        }

    }

    private void send404() throws IOException {
        sendResponse("HTTP/1.1 404 Not Found\r\n\r\n");
    }

}
