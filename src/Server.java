import java.io.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server{
	private Hashtable<String,String> userpass=new Hashtable<String,String>();
	private Hashtable<String,Socket> onLineUser=new Hashtable<String,Socket>();
	private Hashtable<String,Long> lastTime=new Hashtable<String,Long>();
	private Hashtable<String,String> blockedUser=new Hashtable<String,String>();
    private int port;
    private static Server serverInstance;
    private Server(int port){
        this.port=port;
    }
    
    void getServer(){
        StringBuffer sb=new StringBuffer();
        try{
            BufferedReader br=new BufferedReader(new FileReader("/Users/shenxinyi/Desktop/try/user_pass.txt"));
            String s=br.readLine();
            while(s!=null){
                String[] str=s.split(" ");
                userpass.put(str[0],str[1]);
                s=br.readLine();
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        // Enumeration en=userpass.keys();
        // while(en.hasMoreElements()){
        //     System.out.println(en.nextElement());
        // }
    	ServerSocket server = null;
    	try {
			server = new ServerSocket(port);
			while (true) {
                Socket socket = server.accept();
                new ServerThread(socket, port).start();
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
        finally{
            try {
                server.close();
                System.out.println("ServerSocket closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
   
//    public void deleteOnLineUser(String username){
//        boolean isDelete= onLineUser.remove(username);
//        System.out.println(isDelete);
//    }
    
    public static synchronized Server getServerInstance(int port){
    	if(serverInstance == null){
    		serverInstance = new Server(port);
    	}
    	return serverInstance;
    }

    
    public Hashtable<String, String> getUserpass() {
    	return userpass;
    }
    
//    public void setUserpass(Hashtable<String, String> userpass) {
//    	this.userpass = userpass;
//    }
    
//    public void setOnLineUser(HashSet<String> onLineUser) {
//    	this.onLineUser = onLineUser;
//    }
    
    public Hashtable<String,Socket> getOnLineUser() {
		return onLineUser;
	}
    
    public Hashtable<String,Long> getLastTime(){
    	return lastTime;
    }
    
    public Hashtable<String,String> getBlockedUser(){
    	return blockedUser;
    }

	public int getPort() {
		return port;
	}
	
	public static void main(String[] args) {
        int connectport=Integer.parseInt(args[0]);
        serverInstance = getServerInstance(connectport);
        serverInstance.getServer();
    }
    

}
