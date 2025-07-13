[![progress-banner](https://backend.codecrafters.io/progress/http-server/d8f671c0-83b7-4d54-87df-a655c2bd827c)](https://app.codecrafters.io/users/codecrafters-bot?r=2qF)

This is a starting point for Java solutions to the "Build Your Own HTTP server" Challenge



# My Notes

## CRLF Line Endings

- `\r` (Carriage Return): Moves the cursor to the beginning of the line (ASCII 13, 0x0D).
- `\n` (Line Feed): Moves the cursor to the next line (ASCII 10, 0x0A).
- `\r\n` (CRLF): Used in protocols like HTTP to mark the end of a line; required to separate HTTP headers and body.
- A CR immediately followed by an LF (`CRLF`, `\r\n`, or `0x0D0A`) moves the cursor to the beginning of the line and then down to the next line.

HTTP Response: 
``` 
// Status line
HTTP/1.1 200 OK
\r\n                          // CRLF that marks the end of the status line

// Headers
Content-Type: text/plain\r\n  // Header that specifies the format of the response body
Content-Length: 3\r\n         // Header that specifies the size of the response body, in bytes
\r\n                          // CRLF that marks the end of the headers

// Response body
abc                           // The string from the request
```