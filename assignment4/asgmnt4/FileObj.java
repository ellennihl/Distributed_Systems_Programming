package asgmnt4;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

public class FileObj implements Serializable {
	
	private static final long serialVersionUID = -2662687341117920496L;
	
	private String fileName;
	private Date modifiedDate;
	private long sizeBytes;
	private File file;

	public FileObj(File f) {
		file = f;
		fileName = f.getName();
		modifiedDate = new Date(f.lastModified());
		sizeBytes = f.length();
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof FileObj))
			return false;
		else {
			FileObj f = (FileObj) o;
			return (f.fileName.equals(this.fileName) && f.modifiedDate.equals(this.modifiedDate) 
					&& f.sizeBytes == this.sizeBytes);
		}
	}
	
	public String toString() {
		return "{fileName="
				+ fileName + ", mod=" + modifiedDate + ", size=" + sizeBytes + "}";
	}

		private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
				fileName = in.readUTF();
				modifiedDate = new Date();
				modifiedDate.setTime(in.readLong());
				sizeBytes = in.readLong();
		}

		private void writeObject(ObjectOutputStream out) throws IOException {
				out.writeUTF(fileName);
				out.writeLong(modifiedDate.getTime());
				out.writeLong(sizeBytes);
		}

}