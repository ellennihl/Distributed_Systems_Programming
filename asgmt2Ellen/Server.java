package asgmt2;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
	public static final int PORT = 7896;
	ArrayList<Connection> c = new ArrayList<Connection>();
	ArrayList<String> superUsers = new ArrayList<String>();
	
	public Server() {
		superUsers.add("Ellen");
		superUsers.add("Leif");
	}
	
	public void addConnection(Connection con) {
		c.add(con);
	}

	public void broadcast(String msg, Connection current) {
		if (msg.equals("q")) {
			msg = current.getUserName() + " left the channel.";
			current.interrupt();// only interrupt, not join. I want to keep going.
			c.remove(current);
			/*
			 * try { current.join(); c.remove(current); } catch (InterruptedException e) {
			 * e.printStackTrace(); }
			 */
		}
		//if superuser says ban ... then remove it
		/*System.out.println(current.getUserName() + "  " + msg);
		if(msg.contains("ban") && superUsers.contains(current.getUserName())) {
			String[] split = msg.split(" ");
			for(Connection con : c) {
					//jag måste skicka q till client också juuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu
				if(con.getUserName().equals(split[2])) {
					msg = "User " + con.getUserName() + " is banned.";
					System.out.println(msg);
					con.interrupt();
					c.remove(con);
					break;
				}
			}
		}*/
		//else {
			for (Connection con : c) {
				if (con != current) {
					con.sendMsgs(msg);
				}
			}
		//}
	}

	public static void main(String args[]) {
		Server s = new Server();
		ServerSocket listenSocket = null;

		try {
			listenSocket = new ServerSocket(PORT);
			while (true) {
				Connection con;
				con = new Connection(listenSocket.accept(), s);
				con.start();
				s.addConnection(con);
			}
		} catch (IOException e) {
			System.out.println("Listen :" + e.getMessage());
		}
	}

}

class Connection extends Thread {
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;
	Scanner sc;
	String userName;
	Server server;
	volatile int count = -1;

	public Connection(Socket aClientSocket, Server server) {
		try {
			clientSocket = aClientSocket;
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
			sc = new Scanner(System.in);
			this.server = server;
		} catch (IOException e) {
			System.out.println("Connection: " + e.getMessage());
		}
		// In the book "this.start();" is here, this is wrong!
	}

	public String getUserName() {
		return userName;
	}

//notifyall when a message has been sent?
	public void run() {// after run, it is automatically terminated, no need to join()
		count++;
		try {
			userName = in.readUTF();
			String msg = "";
			System.out.println("connected with user: " + userName);
			while (!interrupted()) {
				// if(in.available()>0) {
				synchronized(this) {
					msg = in.readUTF();
					server.broadcast(msg, this);
				}
			}
			in.close();
			out.close();
			System.out.println("closing");
			// clientSocket.close();
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO:" + e.getMessage());
		} finally {

		}

	}

	public void sendMsgs(String msg) {
		try {
			out.writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}