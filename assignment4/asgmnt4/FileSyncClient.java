package asgmnt4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import asgmt2.Server;

public class FileSyncClient implements Serializable {

	private static final long serialVersionUID = -2662687341117920496L;
	
	private String fileName;
	private long modifiedDate;
	private long sizeBytes;

	private Set<File> files;
	//private File file;

	public FileSyncClient() {
		files = new HashSet<File>();
	}

	public void addFile(File f) {
		files.add(f);
	}

	public String toString() {
		/*String ret = "";
		int i = 0;
		Iterator<File> fileIterator = files.iterator();
		while (fileIterator.hasNext()) {
			File f = fileIterator.next();
			ret = ret + "File " + i++ + ": \nfileName="
			+ f.getName() + "mod=" + f.lastModified() + "size=" + f.length() + "\n";
		}
		return ret;*/
		return "fileName="
				+ fileName + "mod=" + modifiedDate + "size=" + sizeBytes + "\n";
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		if(files==null)
			System.out.println("innan read");
		while(in.available()>0) {
			System.out.println("hej");
			fileName = in.readUTF();
			modifiedDate = in.readLong();
		}
		if(files==null)
			System.out.println("efter read");
		// sizeBytes = in.readLong();
		// dateOpened = new Date();
		// dateOpened.setTime(in.readLong());
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		Iterator<File> fileIt = files.iterator();
		if(files==null)
			System.out.println("innan iterate");
		while (fileIt.hasNext()) {
			File f = fileIt.next();
			System.out.println("hej2");
			out.writeUTF(f.getName());
			out.writeLong(f.lastModified());
			// out.writeLong(fileIt.next().length());
		}
		if(files==null)
			System.out.println("efter iterate");
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {

		Socket s = null;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		try {
			s = new Socket("localhost", FileSyncServer.PORT);
			//in = new ObjectInputStream(s.getInputStream());
			out = new ObjectOutputStream(s.getOutputStream());
					//new FileOutputStream("user.dat"));//new ObjectOutputStream(s.getOutputStream());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File newFile = new File(
				"C:\\Users\\ellen\\eclipse-workspace\\Distributed Systems"
		+ " Programming\\src\\asgmnt4\\test2.txt");
		File newFile2 = new File(
				"C:\\Users\\ellen\\eclipse-workspace\\Distributed Systems"
		+ " Programming\\src\\asgmnt4\\test1.txt");
		System.out.println(newFile.getName() + " " + newFile.length() + " " + newFile.lastModified());
		if (newFile.createNewFile()) {
			System.out.println("new file created: " + newFile.getName());
		}

		FileSyncClient fsc = new FileSyncClient();
		System.out.println("new client");
		fsc.addFile(newFile);
		fsc.addFile(newFile2);
		//ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("user.dat"));
		
		out.writeObject(fsc);
		System.out.println("writeobj");
		s.close();
		out.close();

	}
	

	

}
