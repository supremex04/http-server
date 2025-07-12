[![progress-banner](https://backend.codecrafters.io/progress/http-server/d8f671c0-83b7-4d54-87df-a655c2bd827c)](https://app.codecrafters.io/users/codecrafters-bot?r=2qF)

This is a starting point for Java solutions to the "Build Your Own HTTP server" Challenge


# Stage 2 & beyond

Note: This section is for stages 2 and beyond.

1. Ensure you have `mvn` installed locally
1. Run `./your_program.sh` to run your program, which is implemented in
   `src/main/java/Main.java`.
1. Commit your changes and run `git push origin master` to submit your solution
   to CodeCrafters. Test output will be streamed to your terminal.

# My notes

CRLF
CR and LF are control characters or bytecode that can be used to mark a line break in a text file.

CR = Carriage Return (\r, 0x0D in hexadecimal, 13 in decimal) — moves the cursor to the beginning of the line without advancing to the next line.
LF = Line Feed (\n, 0x0A in hexadecimal, 10 in decimal) — moves the cursor down to the next line without returning to the beginning of the line.
A CR immediately followed by a LF (CRLF, \r\n, or 0x0D0A) moves the cursor to the beginning of the line and then down to the next line.