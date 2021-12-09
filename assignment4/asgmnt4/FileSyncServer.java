package asgmnt4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.net.ServerSocket;
import java.net.Socket;

public class FileSyncServer {
	public static final int PORT = 7896;

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		ServerSocket s = null;
		Socket client = null;
		try {
			s = new ServerSocket(PORT);
			client = s.accept();
			System.out.println("accepted");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		ObjectInputStream in = new ObjectInputStream(client.getInputStream());
				//new ObjectInputStream(new FileInputStream("user.dat"));//client.getInputStream());
		//ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
		System.out.println("hej");
		//ObjectInputStream in = new ObjectInputStream(new FileInputStream("user.dat"));
		FileSyncClient myDetailsFromDisk = (FileSyncClient)in.readObject();
        System.out.println(myDetailsFromDisk);
        //I need to verify the client by looking at its serialversionuid. 
        long hej = ObjectStreamClass.lookup(myDetailsFromDisk.getClass()).getSerialVersionUID();
        System.out.println(hej); 
        
        s.close();
        client.close();
        in.close();
        
	}
	

}
