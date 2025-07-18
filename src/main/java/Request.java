import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private BufferedReader reader;
    private String requestLine;
    private String method;
    private String path;
    private String version;
    private Map<String, String> headers;

    public Request(BufferedReader reader) throws IOException {
        this.reader = reader;
        this.headers = new HashMap<>();
        parseRequest();

    }

    private void parseRequest() throws IOException {
        String requestLine = reader.readLine();
        this.requestLine = requestLine;
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IOException("Empty request line");
        }
        String[] parts = requestLine.split("\\s+");
        if (parts.length != 3) {
            throw new IOException("Malformed request line: " + requestLine);
        }
        this.method = parts[0];
        this.path = parts[1].toLowerCase();
        this.version = parts[2];
        String headerLine;
        // isEmpty() is used to check till end of headers (in HTTP, blank line separates
        // headers and body)
        // null is checked for end of stream (connection closed or unexpected EOF)
        while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
            // each header name ends with colon
            // eg: User-Agent: curl/7.79.1
            int colonIndex = headerLine.indexOf(':');
            // if no colon is found index is -1
            if (colonIndex != -1) {
                String headerName = headerLine.substring(0, colonIndex).trim().toLowerCase();
                String headerValue = headerLine.substring(colonIndex + 1).trim();
                headers.put(headerName, headerValue);
            }
        }
        String requestBody = null;
        if (headers.containsKey("content-length")) {
            int contentLength = Integer.parseInt(headers.get("content-length"));
            char[] bodyChars = new char[contentLength];
            int read = reader.read(bodyChars, 0, contentLength);
            requestBody = new String(bodyChars, 0, read);
            headers.put("body", requestBody);
        }

    }

    public String getrequestLine() {
        return this.requestLine;
    }

    public String getMethod() {
        return this.method;
    }

    public String getPath() {
        return this.path;
    }

    public String getVersion() {
        return this.version;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public String getHeader(String name) {
        return this.headers.get(name.toLowerCase());
    }

}
