import java.io.BufferedWriter;
import java.io.IOException;
public class RequestHandler {
    private final Request request;
    private final BufferedWriter writer;
    public RequestHandler( Request request, BufferedWriter writer){
        this.request = request;
        this.writer = writer;
    }

    private void sendResponse(String response) throws IOException {
        writer.write(response);
        writer.flush();
    }

    public void handle() throws IOException{
        String method = request.getMethod();
        String path = request.getPath();
        if (method.equals("GET")) {
                    if (path.equals("/")) {
                        sendResponse("HTTP/1.1 200 OK\r\n\r\n");
                    } else if (path.split("/")[1].equals("echo")) {
                        String echo = request.getPath().split("/")[2]; // first is empty string, second is "echo", third is payload
                        String fullResponse = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: "
                                + echo.length() + "\r\n\r\n" + echo;
                        sendResponse(fullResponse);
                    } else if (path.split("/")[1].equals("user-agent")) {
                        String userAgent = request.getHeader("user-agent");
                        String fullResponse = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: "
                                + userAgent.length() + "\r\n\r\n" + userAgent;
                        sendResponse(fullResponse);

                    } else if (path.split("/")[1].equals("index.html")) {
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
