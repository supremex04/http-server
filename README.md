

# My Notes

## CRLF Line Endings

- `\r` (Carriage Return): Moves the cursor to the beginning of the line (ASCII 13, 0x0D).
- `\n` (Line Feed): Moves the cursor to the next line (ASCII 10, 0x0A).
- `\r\n` (CRLF): Used in protocols like HTTP to mark the end of a line; required to separate HTTP headers and body.
- A CR immediately followed by an LF (`CRLF`, `\r\n`, or `0x0D0A`) moves the cursor to the beginning of the line and then down to the next line.


HTTP Request: 
```
// Request line
GET
/user-agent (--path)
HTTP/1.1
\r\n

// Headers
Host: localhost:4221\r\n
User-Agent: foobar/1.2.3\r\n  // Read this value
Accept: */*\r\n
\r\n

// Request body (empty)
```
HTTP Response: 
``` 
// Status line
HTTP/1.1 200 OK               // Status code must be 200
\r\n

// Headers
Content-Type: text/plain\r\n
Content-Length: 12\r\n
\r\n

// Response body
foobar/1.2.3                  // The value of `User-Agent`                        // The string from the request
```

```BufferedReader``` and ```BufferedWriter``` internally convert byte<>string

Commands:
```
$ curl -v 127.0.0.1:4221/echo/abc
$ curl -v --header "User-Agent: foobar/1.2.3" http://localhost:4221/user-agent
```