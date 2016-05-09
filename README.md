# CNSocketProgramming
1. Description:
The socket programming assignment is implemented with JAVA.
I make mainly five classes, they are Server, ServerThread, Client, ClientListener, ClientSender.
Server is first running on a certain port (4119) waiting for the Client to connect to it.
Once a Client connect to the server socket, the server create a new thread ServerThread for the client, receiving and sending messages, verify user login, logout and react to users’ command. This approach allows multi-Client connect to the running server.
A Client connecting to the running server should first know the server’s ip address and port number. once being authenticated, the new client create a new ClientListener which used to listen to the message from the socket, and a new ClientSender which is used to send default reaction or user input to the server.
There are also other classes. One is the class used to create userpass.txt file including the SHA1 function.  Another one is the inner class MyThread used as a handler when keyboard interruption occurs in both ServerThread and ClientSender class.

2. Detail on development environment
Language: JAVA SE-1.6
OS: MAC OS 10.10.5
Editor: Sublime Text 2

3. Instructions on running the code
First using the terminal enter the directory containing all the .java files
Then input “make” in the command line, if there is not error, all the java files have been compiled
After that input “java Server 4119”, if there is not error, the Server is running and listening on port 4119
After the server is running, to add a client, simply run “java Client 127.0.0.1(or the ip address that the server is running)”in a new terminal 
After logged in, there will be instruction about what you should input.

4. Sample commands to invoke code
“make”
“java Server 4119”
“java Client 127.0.0.1(server’s ip address) 4119”
