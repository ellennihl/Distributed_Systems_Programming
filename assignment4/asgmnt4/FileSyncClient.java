package asgmnt4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FileSyncClient implements Serializable {

	private static final long serialVersionUID = -2662687341117920496L;

	private String fileName;
	private long modifiedDate;
	private long sizeBytes;

	private Set<File> files = new HashSet<File>();
	//private File file;

	public FileSyncClient() {
		// file = f;
		

	}

	public void addFile(File f) {
		files.add(f);
	}
	//Det är nåt fel med iterator!!!!!!!!!!!!!!!!! det e därför min return e konstig
	public String toString() {
		/*String ret = "";
		int i = 0;
		Iterator<File> fileIt = files.iterator();
		while (fileIt.hasNext()) {
			File f = fileIt.next();
			ret = ret + "File " + i++ + ": \nfileName="
			+ f.getName() + "mod=" + f.lastModified() + "size=" + f.length() + "\n";
		}
		return ret;*/
		
		return "fileName="
			+ fileName + "mod=" + modifiedDate + "size=" + sizeBytes + "\n";
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		while(in.available()>0) {
			System.out.println("hej");
			fileName = in.readUTF();
			modifiedDate = in.readLong();
		}
		// sizeBytes = in.readLong();
		// dateOpened = new Date();
		// dateOpened.setTime(in.readLong());
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		Iterator<File> fileIt = files.iterator();
		while (fileIt.hasNext()) {
			File f = fileIt.next();
			System.out.println("hej2");
			out.writeUTF(f.getName());
			out.writeLong(f.lastModified());
			// out.writeLong(fileIt.next().length());
		}
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
		fsc.addFile(newFile);
		fsc.addFile(newFile2);
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("user.dat"));

		out.writeObject(fsc);

		out.close();

	}
}
