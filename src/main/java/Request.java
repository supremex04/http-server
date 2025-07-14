import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



public class Request {
    private BufferedReader  reader;
    private String method;
    private String path;
    private String version;
    private Map<String, String> headers;

    public Request (BufferedReader reader) throws IOException{
        this.headers = new HashMap<>();
        parseRequest();


    }

    private void parseRequest() throws IOException{
        String requestLine = reader.readLine();
         if (requestLine == null || requestLine.isEmpty()) {
            throw new IOException("Empty request line");
        }
        String[] parts = requestLine.split("\\s+");
        if (parts.length != 3) {
            throw new IOException("Malformed request line: " + requestLine);
        }
        this.method = parts[0];
        this.path = parts[1];
        this.version = parts[2];
        String headerLine;



    }

    

}
