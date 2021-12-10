package Task4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class FileObj implements Serializable, Comparable<FileObj>{

	private static final long serialVersionUID = -7837545380407702492L;
	
	private String name;
	private Date lastUpdate;
	private long size;
	
	public FileObj(String name, Date lastUp, long size) {
		this.name = name;
		lastUpdate = lastUp;
		this.size = size;
	}
	
	public String getName() {return name;}
	public Date getLastUpdate() {return lastUpdate;}
	
	public String toString() {
        return "File[Name="+name+",lastUpdate="+lastUpdate+",size="+size+"]";
    }
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		name = in.readUTF();
		lastUpdate = new Date();
		lastUpdate.setTime(in.readLong());
		size = in.readLong();
    }
	
	private void writeObject(ObjectOutputStream out) throws IOException {
    	out.writeUTF(name);
    	out.writeLong(lastUpdate.getTime());
    	out.writeLong(size);
    }
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {

		 //Creating a File object for directory
        File directoryPath = new File("C:\\Users\\Eek\\eclipse-workspace\\Distributed Systems\\src");
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
        
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("user.dat"));
        for (FileObj fileObj : objs) {
        	out.writeObject(fileObj);
		}
        
        Socket client = null;
        ObjectInputStream in = null;

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

	@Override
	public int compareTo(FileObj o) {
		
		if(this.name.equals(o.name))
			return 0;
		return 0;
	}
}
