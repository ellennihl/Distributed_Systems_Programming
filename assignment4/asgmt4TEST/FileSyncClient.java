package asgmnt4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FileSyncClient {

	Socket s;
	ObjectInputStream in;
	ObjectOutputStream out;
	public Set<FileObj> files;
	//private File file;

	public FileSyncClient() {
		try {
			s = new Socket("localhost", FileSyncServer.PORT);
			//in = new ObjectInputStream(s.getInputStream());
			out = new ObjectOutputStream(s.getOutputStream());
					//new FileOutputStream("user.dat"));//new ObjectOutputStream(s.getOutputStream());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		files = new HashSet<FileObj>();
	}
	
	public void addFile(FileObj f) {
		files.add(f);
	}
	
	public void write() throws IOException {
		out.writeObject(files);
		s.close();
		out.close();
		//out.writeObject(fo2);
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		
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
		FileObj fo1 = new FileObj(newFile);
		FileObj fo2 = new FileObj(newFile2);
		fsc.addFile(fo1);
		fsc.addFile(fo2);
		//ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("user.dat"));
		
		fsc.write();
		
		System.out.println("writeobj");


	}
/*
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
		/*return "fileName="
				+ fileName + "mod=" + modifiedDate + "size=" + sizeBytes + "\n";
	}*/

/*	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
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
	}*/

/*	private void writeObject(ObjectOutputStream out) throws IOException {
		Iterator<File> fileIt = files.iterator();
		if(files==null)
			System.out.println("innan iterate");
		while (fileIt.hasNext()) {
			File f = fileIt.next();
			out.writeUTF(f.getName());
			out.writeLong(f.lastModified());
			// out.writeLong(fileIt.next().length());
		}
		if(files==null)
			System.out.println("efter iterate");
	}*/
	
/*	public class FileNameComparator implements Comparator<FileSyncClient> {
		 
		@Override
		public int compare(FileSyncClient f1, FileSyncClient f2) {
			return f1.fileName.compareTo(f2.fileName);
		}
	}*/
	

	
}
