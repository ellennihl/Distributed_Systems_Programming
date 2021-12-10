package asgmnt4;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;

public class FileObj implements Serializable {
	
	private static final long serialVersionUID = -2662687341117920496L;
	
	private String fileName;
	private long modifiedDate;
	private long sizeBytes;
	private File file;

	public FileObj(File f) {
		file = f;
		fileName = f.getName();
		modifiedDate = f.lastModified();
		sizeBytes = f.length();
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
				fileName = in.readUTF();
				modifiedDate = in.readLong();
				sizeBytes = in.readLong();
			// dateOpened = new Date();
			// dateOpened.setTime(in.readLong());
		}

		private void writeObject(ObjectOutputStream out) throws IOException {
				out.writeUTF(fileName);
				out.writeLong(modifiedDate);
				out.writeLong(sizeBytes);
				// out.writeLong(fileIt.next().length());
		}

}