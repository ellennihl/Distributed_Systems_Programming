package asgmnt4;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class FileSyncServer {
	public static final int PORT = 7896;

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		ServerSocket s = null;
		Socket client = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		ArrayList<FileObj> db = new ArrayList<FileObj>();
		ArrayList<FileObj> syncDb = new ArrayList<FileObj>();
		while (true) {
			try {
				s = new ServerSocket(PORT);
				client = s.accept();
				System.out.println("accepted");
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			out = new ObjectOutputStream(client.getOutputStream());
			in = new ObjectInputStream(client.getInputStream());
			ArrayList<FileObj> fo = (ArrayList<FileObj>) in.readObject();
			for (FileObj readObj : fo) {
				boolean found = false;
				for (FileObj dbObj : db) {
					if (readObj.equals(dbObj)) {
						found = true;
						break;
					}
				}
				if (!found) {
					syncDb.add(readObj);
					db.add(readObj);
				}

			}
			System.out.println("sync:\n" + syncDb);
			out.writeObject(syncDb);
			syncDb.clear();

			s.close();
			client.close();
			out.close();
			in.close();
		}

	}
}
