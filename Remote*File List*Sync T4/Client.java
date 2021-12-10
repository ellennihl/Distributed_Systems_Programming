package Task4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class Client{
	
	public static FileObj[] getFileObjFromDict(String path) {
		File directoryPath = new File(path);
	       //List of all files and directories
	       File files[] = directoryPath.listFiles();
	       FileObj objs[] = new FileObj[files.length];
	       for (int i = 0; i < files.length; i++) {
	       	objs[i] = new FileObj(
	       			files[i].getName(),
	       	        new Date(files[i].lastModified()),
	       	        files[i].getTotalSpace());
	       	System.out.println(objs[i]);
			}
	       return objs;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {

		FileObj objs[] = getFileObjFromDict("C:\\Users\\Eek\\eclipse-workspace\\Distributed Systems\\src");
       
		//inte spara ner
		/*ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("user.dat"));
		for (FileObj fileObj : objs) {
			out.writeObject(fileObj);
		}*/
       
		Socket client = null;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;

		try {
			client = new Socket("127.0.0.1", 8888);
			out = new ObjectOutputStream(client.getOutputStream());
			in = new ObjectInputStream(client.getInputStream());
			
			for (FileObj fileObj : objs) {
				out.writeObject(fileObj);
			}
			out.flush();
			
			//close resources
			out.close();
			in.close();
			client.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}

