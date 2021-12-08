package asgmnt4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class FileSyncServer {
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("user.dat"));
		FileSyncClient myDetailsFromDisk = (FileSyncClient)in.readObject();
        System.out.println(myDetailsFromDisk);
        //I need to verify the client by looking at its serialversionuid. 
        long hej = ObjectStreamClass.lookup(myDetailsFromDisk.getClass()).getSerialVersionUID();
        System.out.println(hej); 
	}
}
