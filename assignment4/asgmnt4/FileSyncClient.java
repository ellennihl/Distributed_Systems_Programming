package asgmnt4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FileSyncClient  {

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("user.dat"));
		FileSyncServer myDetailsFromDisk = (FileSyncServer)in.readObject();
        System.out.println(myDetailsFromDisk);
        //I need to verify the client by looking at its serialversionuid. 
        long hej = ObjectStreamClass.lookup(myDetailsFromDisk.getClass()).getSerialVersionUID();
        System.out.println(hej); 
	}
}
