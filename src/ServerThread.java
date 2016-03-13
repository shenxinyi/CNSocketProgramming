import java.io.*;
import java.net.*;
import java.util.*;
import java.net.*;
import java.util.Iterator;
import java.io.UnsupportedEncodingException; 
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;

public class ServerThread extends Thread {
	private Socket socket;
	private BufferedReader socketInput;
    private PrintWriter output;
    private String username;
    private String password;
    private String command;
    private String ipaddr;
    private long minutes;
    private String lastname;
    private Date date;
    private String[] commandDetail;
    private int countWrongUsername=0;
    private int countWrongPassword=0;
    private static final int BLOCK_TIME=60000;
    private int port;
  
    public ServerThread(Socket s, int port)throws IOException {
       socket = s; 
       this.port = port;
       socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
       output = new PrintWriter(socket.getOutputStream(), true);
    }
    class User{
        
    }
  
    public void run() {
        try{
        	//Timer timer=new Timer();
            String line = socketInput.readLine();
            //System.out.println("Client says:"+line);
            if (line.equals("Hello!")) {
                write("username:");
            }
            username=socketInput.readLine();
            ipaddr=InetAddress.getLocalHost().toString();
            if(Server.getServerInstance(port).getUserpass().containsKey(username)){
                while(Server.getServerInstance(port).getOnLineUser().contains(username)||(Server.getServerInstance(port).getBlockedUser().containsKey(username)&&Server.getServerInstance(port).getBlockedUser().get(username)==ipaddr)) {
                    write(username+" is already online. Try other user:");
                    username=socketInput.readLine();
                }
                write("Hi "+username+", please enter password:");
            } 
            while(!Server.getServerInstance(port).getUserpass().containsKey(username)) {
                write("no username "+username+" in the system. Enter right username");
                countWrongUsername++;
                if(countWrongUsername>2){
                    write("rest for one minute");
                    ipaddr=InetAddress.getLocalHost().toString();
                    Server.getServerInstance(port).getBlockedUser().put(username, ipaddr);
                    try{
                        this.sleep(BLOCK_TIME);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    Server.getServerInstance(port).getBlockedUser().remove(username);
                    countWrongUsername=0;
                }
                username=socketInput.readLine();
            }
            
            try{
                password=sha1(socketInput.readLine());
            }catch(NoSuchAlgorithmException e){
                e.printStackTrace();
            }
            
            if(Server.getServerInstance(port).getUserpass().get(username).equals(password)) write("Welcome to the simple chat server!");
            Server.getServerInstance(port).getLastTime().put(username, 0l);
            while(!Server.getServerInstance(port).getUserpass().get(username).equals(password)){
                write("Wrong password for user "+username+" enter again:");
                countWrongPassword++;
                if(countWrongPassword>2){
                    write("rest for one minute");
                    try{
                        this.sleep(BLOCK_TIME);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    countWrongPassword=0;
                }
                try{
                    password=sha1(socketInput.readLine());
                }catch(NoSuchAlgorithmException e){
                    e.printStackTrace();
                }
            }
            Server.getServerInstance(port).getOnLineUser().put(username,socket);
            write("Command:");
            command=socketInput.readLine();
            while(!command.equals("logout")){
                if(command.equals("who")){
                    Hashtable<String,Socket> sh=Server.getServerInstance(port).getOnLineUser();
                    Enumeration<String> e=sh.keys();
                	while(e.hasMoreElements()){
                		lastname= e.nextElement();
                		if(!lastname.equals(username)) write(lastname);
                	}
                }else 
                if(command.startsWith("last ")){
                	commandDetail=command.split(" ");
                	minutes=Long.parseLong(commandDetail[1], 10)*1000*60;
                	date=new Date();
                	Hashtable<String,Long> lt=Server.getServerInstance(port).getLastTime();
                	Enumeration<String> e=lt.keys();
                	while(e.hasMoreElements()){
                		lastname=e.nextElement();
                		if(lt.get(lastname)==0l){
                			write(lastname);
                		}else{
                			Long dif=date.getTime()-lt.get(lastname);
                			if(dif<minutes) write(lastname);
                		}
                	}
                	
                }else 
                if(command.startsWith("broadcast ")){
                	//command=command.substring(10);
                	Hashtable<String,Socket> ht=Server.getServerInstance(port).getOnLineUser();
                	Enumeration<String> e=ht.keys();
                	while(e.hasMoreElements()){
                		lastname=e.nextElement();
                		if(lastname.equals(username)) continue;
                		PrintWriter broadcast=new PrintWriter(ht.get(lastname).getOutputStream(), true);
                    	broadcast.write(username+": "+command.substring(10));
                    	broadcast.write("\n");
                    	broadcast.flush();
                	}
                }else
                if(command.startsWith("send ")){
                	Hashtable<String,Socket> ht=Server.getServerInstance(port).getOnLineUser();
                	int starti=command.indexOf('(');
                	if(starti!=5){
                		lastname=command.substring(5).split(" ")[0];
                		starti=command.substring(5).indexOf(" ");
                		
                		if(ht.containsKey(lastname)){
                			PrintWriter broadcast=new PrintWriter(ht.get(lastname).getOutputStream(), true);
                        	broadcast.write(username+": "+command.substring(5).substring(starti+1));
                        	broadcast.write("\n");
                        	broadcast.flush();
                		}else write(lastname+" is not online.");
                	}else{
                		int endi=command.indexOf(')');
                		String[] names=command.substring(starti,endi+1).split(" ");
                		for(String name:names){
                			if(ht.containsKey(name)){
                				PrintWriter broadcast=new PrintWriter(ht.get(lastname).getOutputStream(), true);
                				broadcast.write(username+": "+command.substring(endi+2));
                				broadcast.write("\n");
                            	broadcast.flush();
                			}else write(name+" is not online.");
                		}	
                	}
                }else write("no such command.");
                command=socketInput.readLine();
            }
            if(command.equals("logout")){
                write("You have already logout.");
                Server.getServerInstance(port).getOnLineUser().remove(username);
                date=new Date();
                Server.getServerInstance(port).getLastTime().put(username,date.getTime());
                socketInput.close();
                output.close();
                socket.close();
            }

        } catch (IOException e) {
                // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    static String sha1(String input) throws NoSuchAlgorithmException{
        MessageDigest mDigest=MessageDigest.getInstance("SHA1");
        byte[] result=mDigest.digest(input.getBytes());
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<result.length;i++){
            sb.append(Integer.toString((result[i]&0xff)+0x100,16).substring(1));
        }
        return sb.toString();
    }
    
    private void write(String command) {
        output.write(command);
        output.write("\n");
        output.flush();
    }
 }