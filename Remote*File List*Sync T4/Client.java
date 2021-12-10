package Task4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
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

		FileObj objs[] = getFileObjFromDict("C:\\Users\\Eek\\OneDrive\\Skrivbord\\katt");
		ArrayList<FileObj> files = new ArrayList<FileObj>();
		ArrayList<FileObj> newfiles = new ArrayList<FileObj>();
		for (FileObj fileObj : objs) {
			files.add(fileObj);
		}
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
			in = new ObjectInputStream(client.getInputStream());
			out = new ObjectOutputStream(client.getOutputStream());
			
			out.writeObject(files);
			//out.flush();
			
			newfiles = (ArrayList<FileObj>)in.readObject();
			System.out.println("Things to backup : ");
			for (FileObj f : newfiles) {
				System.out.println(f);
			}
			
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

