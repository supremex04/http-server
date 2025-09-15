
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.zip.GZIPOutputStream;





public class RequestHandler {

    private final Request request;
    private final BufferedWriter writer;
    private final OutputStream rawOut;  // for binary responses


    public RequestHandler(Request request, BufferedWriter writer, OutputStream rawOut) {
        this.request = request;
        this.writer = writer;
        this.rawOut = rawOut;
    }

    private void sendResponse(String response) throws IOException {
        writer.write(response);
        writer.flush();
    }

    private void sendGzipResponse(String body) throws IOException {
    byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

    // Compress
    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
        gzipStream.write(bodyBytes);

    }
    byte[] gzippedBytes = byteStream.toByteArray();

    // Build headers
    String headers = "HTTP/1.1 200 OK\r\n" +
                     "Content-Type: text/plain\r\n" +
                     "Content-Encoding: gzip\r\n" +
                     "Content-Length: " + gzippedBytes.length + "\r\n\r\n";

    // Write headers + body as raw bytes
    rawOut.write(headers.getBytes(StandardCharsets.US_ASCII));
    rawOut.write(gzippedBytes);
    rawOut.flush();
}



    public void handle() throws IOException {

        String method = request.getMethod();
        String[] path = request.getPath().split("/");
        if (method.equals("GET")) {
            if (request.getPath().equals("/")) {
                sendResponse("HTTP/1.1 200 OK\r\n\r\n");
            } 
            else if (path.length > 1 && path[1].equals("echo")) {
                String echo = path.length > 2 ? path[2] : "";
                String encoding = request.getHeader("accept-encoding");

                if (encoding != null && encoding.contains("gzip")) {
                    sendGzipResponse(echo);
                } else {
                    String fullResponse = "HTTP/1.1 200 OK\r\n"
                            + "Content-Type: text/plain\r\n"
                            + "Content-Length: " + echo.length() + "\r\n\r\n"
                            + echo;
                    sendResponse(fullResponse);
                }
    }
            else if (path[1].equals("user-agent")) {
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
                } else {
                    send404();
                }

            } else if (path[1].equals("index.html")) {
                sendResponse("HTTP/1.1 200 OK\r\n\r\n");
            } else {
                send404();
            }
        }

        if (method.equals("POST")) {
            if (path[1].equals("files")) {
                // converting String to integer
                // int contentLength = Integer.parseInt(request.getHeader("Content-Length"));
                String filePath = Main.map.get("dir") + path[2];
                String requestBody = request.getHeader("body");
                try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath))) {
                    fileWriter.write(requestBody);
                    sendResponse("HTTP/1.1 201 Created\r\n\r\n");
                } catch (IOException e) {
                    send404();
                    System.out.println("Error writing on file: " + e.getMessage());;
                }

            }
        }

    }

    private void send404() throws IOException {
        sendResponse("HTTP/1.1 404 Not Found\r\n\r\n");
    }

}
