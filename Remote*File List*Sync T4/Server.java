package Task4;

import java.io.EOFException;
import java.io.FileInputStream;
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
		/*
		
		}*/
		/*ObjectInputStream in = new ObjectInputStream(new FileInputStream("user.dat"));
		ObjectOutputStream out = null;*/
		//ArrayList<FileObj> database = getFromDat(in);
		
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
			in = new ObjectInputStream(client.getInputStream());    // get the input stream of client.
			
			try {
				while(true) {
					newData.add((FileObj)in.readObject());
				}
			}
			catch(EOFException exc){
				//TODO Gämför och skicka tillbaka
				for (FileObj f : newData) {
					for (FileObj db : database) {
						if(f.getName().equals(db.getName())) {
							if(f.getLastUpdate().compareTo(db.getLastUpdate()) == 0) {
								System.out.println("same");
							} else {
								System.out.println("different or new");
							}
						}
					}
				}
				
				
				for (FileObj fileObj : database) {
					System.out.println(fileObj);
				}
				System.out.println("End");
			}
			
			// close resources
			out.close();
			in.close();
			client.close();
			server.close();
			newData.clear();
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
