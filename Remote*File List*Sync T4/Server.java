package Task4;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	public static ArrayList<FileObj> getFromDat(ObjectInputStream in) throws ClassNotFoundException, IOException {
		ArrayList<FileObj> tmp = new ArrayList<FileObj>();
		try {
			while(true) {
				tmp.add((FileObj)in.readObject());
			}
		}
		catch(EOFException exc){
			System.out.println("End");
			return tmp;
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		ArrayList<FileObj> database = new ArrayList<FileObj>();
		ArrayList<FileObj> newData = new ArrayList<FileObj>();
		Socket client = null;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		
		try {
			while(true) {
			ServerSocket server = new ServerSocket(8888); // start listening on port 8888
			client = server.accept(); // this method is a blocking I/O call, it will not be called unless
			//a connection is established.
			
			out = new ObjectOutputStream(client.getOutputStream()); // get the output stream of client.
			in = new ObjectInputStream(client.getInputStream());   // get the input stream of client.
			
			newData = (ArrayList<FileObj>)in.readObject();
				//TODO compare of thay are the same
				ArrayList<FileObj> different = new ArrayList<FileObj>();
				ArrayList<FileObj> toRemove = new ArrayList<FileObj>();
				ArrayList<FileObj> same = new ArrayList<FileObj>();
				for (FileObj f : newData) {
					for (FileObj db : database) {
						if(f.getName().equals(db.getName())) {
							if(f.getLastUpdate().compareTo(db.getLastUpdate()) == 0) {
								System.out.println("same " + f.getName());
								same.add(f);
							} else {
								System.out.println("different " + f.getName());
								different.add(f);
								toRemove.add(db);
							}
						}
					}
				}
				
				database.removeAll(toRemove);
				newData.removeAll(same);
				newData.removeAll(different);
				database.addAll(different);
				
				if(!newData.isEmpty()) {
					for (FileObj f : newData) {
						System.out.println("New file " + f);
						database.add(f);
						different.add(f);
					}
				}
				System.out.println("End \r\n \r\n");
			
				out.writeObject(different);
			
			// close resources
			out.close();
			in.close();
			client.close();
			server.close();
			newData.clear();
			toRemove.clear();
			different.clear();
			same.clear();
			}
			
			} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
			} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
			}
	}
}
