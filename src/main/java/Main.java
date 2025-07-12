import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args) {
    System.out.println("Logs from your program will appear here!");

    
    try {
      // ServerSocket creates a server that listens for incoming TCP connections on port 4221.
      // This socket does NOT send or receive data directly — it only accepts new client connections.
      ServerSocket serverSocket = new ServerSocket(4221);
    

      serverSocket.setReuseAddress(true);
      // Socket handles communication with a connected client (active)
      Socket clientSocket = serverSocket.accept(); // Wait for connection from client.
      System.out.println("accepted new connection");
      String payload = "HTTP/1.1 200 OK\r\n\r\n";
      // OutputStream is used to send data to the client.
      // It is obtained from the Socket object.
      // The OutputStream is buffered, meaning it collects data before sending it over the network.
      // This is why we need to call flush() to ensure the data is sent immediately.
      // Think of this as opening a speaker/microphone line to the person you’re talking to.
      // The OutputStream is not the same as the Socket, which is used for reading and writing data.
      // The OutputStream is like a channel through which you send your data.
      // The Socket is like the connection to the person you’re talking to.
      OutputStream out = clientSocket.getOutputStream();
      // out.write(...)	Writes bytes into a buffer, not directly to the socket.
      out.write(payload.getBytes());
      // out.flush()	Actually sends the buffer’s content over the network.
      out.flush();
      clientSocket.close(); // Close the connection with the client.
      serverSocket.close(); // Close the server socket.
      System.out.println("closed connection");
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}
