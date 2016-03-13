import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientSender extends Thread {

    private BufferedReader stdIn;
    private PrintWriter output;
    private Socket socket;
    private static final int BLOCK_TIME=18000000;

    public ClientSender(PrintWriter output, BufferedReader stdIn, Socket socket) {
        this.output = output;
        this.stdIn = stdIn;
        this.socket = socket;
    }
    class TimeOutLogOut extends TimerTask{
        public void run(){
        	try{
        		write("logout");
                stdIn.close();
                output.close();
                socket.close();
        	}catch(IOException e) {
                e.printStackTrace();
            }
            
        }
    }

    public void run() {
        String command = null;
        try {
            while (true) {
            	Timer timer=new Timer();
                timer.schedule(new TimeOutLogOut(),BLOCK_TIME);
                command = stdIn.readLine();
                if (command.equals("")) {
                    continue;
                }
                
                if (command.equals("logout")){
                    write(command);
                    stdIn.close();
                    output.close();
                    socket.close();
                    break;
                }
                write(command);
                timer.purge();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(String command) {
        output.write(command);
        output.write("\n");
        output.flush();
    }
}
