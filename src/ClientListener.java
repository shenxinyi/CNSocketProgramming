import java.io.*;

public class ClientListener extends Thread {

    private BufferedReader socketInput;

    public ClientListener(BufferedReader socketInput) {
        this.socketInput = socketInput;
    }

    public void run() {
        while (true) {
            String response = null;
            try {
                response = socketInput.readLine();
                System.out.println(response);
                
            } catch (IOException e) {
                try {
                    socketInput.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (NullPointerException e) {
            }
        }
    }
}
