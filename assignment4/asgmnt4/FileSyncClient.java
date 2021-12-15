package asgmnt4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
public class FileSyncClient {

	Socket s;
	ObjectInputStream in;
	ObjectOutputStream out;
	public ArrayList<FileObj> files;

	public FileSyncClient() {
		try {
			s = new Socket("localhost", FileSyncServer.PORT);
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		files = new ArrayList<FileObj>();
	}
	
	public void addFile(File f) throws IOException {
		FileObj fo = new FileObj(f);
		if (f.createNewFile()) {
			System.out.println("new file created: " + f.getName());
		}
		files.add(fo);
		
	}
	
	public void sync() throws IOException, ClassNotFoundException, InterruptedException {
		out.writeObject(files);
		ArrayList<FileObj> backup = (ArrayList<FileObj>) in.readObject();
		System.out.println(backup);

		s.close();
		out.close();
		in.close();
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, InterruptedException {

		FileSyncClient fsc = new FileSyncClient();

		File dir = new File("files");//put the files in the same project folder
		File[] dirContents = dir.listFiles();
		if (dirContents != null) {
		  for (File child : dirContents) {
			  fsc.addFile(child);
		  }
		}
		
		fsc.sync();

	}
}