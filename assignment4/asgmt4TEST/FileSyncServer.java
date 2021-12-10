package asgmnt4;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class FileSyncServer {
	public static final int PORT = 7896;

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		ServerSocket s = null;
		Socket client = null;
		Set<FileObj> db = new HashSet<FileObj>();
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

		//ObjectInputStream in = new ObjectInputStream(new FileInputStream("user.dat"));
		while(!client.isClosed()) {
			//if(in.available()>0) {
				FileObj fo = (FileObj)in.readObject();
				System.out.println(fo);
				//Collections.binarySearch(files, fsc, new FileNameComparator());
				if(!db.contains(fo)) {
					db.add(fo);
					System.out.println("add file");
				}
			
		}
		
		
        //System.out.println(myDetailsFromDisk);
        //I need to verify the client by looking at its serialversionuid. 
        //long hej = ObjectStreamClass.lookup(myDetailsFromDisk.getClass()).getSerialVersionUID();
        //System.out.println(hej); 
        
        s.close();
        client.close();
        in.close();
        
	}
	

}
