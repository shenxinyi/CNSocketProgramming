import java.io.UnsupportedEncodingException; 
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class CreateUserPass{
	public static void main(String[] args) throws NoSuchAlgorithmException{
		String[] username= {"columbia","seas","csee4119","foobar","windows","google","facebook","wikipedia","network"};
		String[] password= {"116way","winterisover","lotsofassignment","passpass","withglass","partofalphabet","wastetime","donation","seemsez"};
		try{
			File file =new File("/Users/shenxinyi/Desktop/try/user_pass.txt");
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fw=new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw=new BufferedWriter(fw);
			for(int i=0;i<username.length;i++){
				bw.write(username[i]);
				bw.write(" ");
				bw.write(sha1(password[i]));
				if(i<username.length-1){
					bw.newLine();
				}
			}
			bw.close();
		}catch(IOException e){
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
}