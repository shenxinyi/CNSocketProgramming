import java.io.*;
import java.net.*;

public class Client {

    private int port;
    private String serverAddress;
    private Socket socket;
    private BufferedReader socketInput;
    private BufferedReader stdIn;
    private PrintWriter output;
    private String userinput;
    private String line;

    public Client() throws Exception {
    	serverAddress = "127.0.0.1";
        port = 4119;
        socket = new Socket(serverAddress, port);
        socketInput = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
        stdIn = new BufferedReader(new InputStreamReader(System.in));
    }
    
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        try {
        	client.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}   
    }

    private void execute() throws IOException {
        write("Hello!");
        line = socketInput.readLine();
        System.out.println(line);
        while(!line.equals("Welcome to the simple chat server!")){
        	userinput=stdIn.readLine();
        	write(userinput);
        	line= socketInput.readLine();
            System.out.println(line);
        }
        line= socketInput.readLine();
        System.out.println(line);
        new ClientListener(socketInput).start();
        new ClientSender(output, stdIn, socket).start();
    }

    private void write(String command) {
        output.write(command);
        output.write("\n");
        output.flush();
    }
}