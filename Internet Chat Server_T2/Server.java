//Eek de Bruijckere, Ellen Nhil.

package internet_chat_service;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Server {
	public static final int PORT = 7897;
	public static ArrayList<Connection> connectedServices; //List of connected clients
	public static ArrayList<PrivateChat> privateSessions; //List of private sessions
	public static final String[] adminList = {"Eek","eek","Eejk"}; //the admin usernames
	
	public static void main(String args[]) {
		try {
			//open a socket to listen to
			ServerSocket listenSocket = new ServerSocket(PORT);
			connectedServices = new ArrayList<Connection>();
			privateSessions = new ArrayList<PrivateChat>();
			
			while(true) {
				new Connection(listenSocket.accept()).start();
			}
		} catch (IOException e) {
			System.out.println("Listen :" + e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param ms message to be sent to all clients connected the server
	 * @param username the user sending the ms.
	 * This function broadcasts a message to all clients connected to the server
	 */
	public static void broadcast(String ms, String username) {
		//send to all messages in the connectServices list
		for(Connection t : connectedServices) {
			try {
				t.getOut().writeUTF(username + ": " + ms);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//send to all clients in the privateSessions list
		for(PrivateChat p : privateSessions) {
			try {
				p.getUser1().getOut().writeUTF(username + ": " + ms);
				p.getUser2().getOut().writeUTF(username + ": " + ms);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void privateBroadcast(String ms, String user) {
		for(PrivateChat p : privateSessions) {
			try {
				if(p.getUser1().username.equals(user) || p.getUser2().username.equals(user)) {
					p.getUser1().getOut().writeUTF(user + ": " + ms);
					p.getUser2().getOut().writeUTF(user + ": " + ms);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void kickUser(String ms, String user) {
		
		String split[] = ms.split(" ");
		System.out.println(split[0] + "  :  " + split[1]);
		
		//if user exists kick the user
		for(Connection t : connectedServices) {
			if(split[1].equals(t.username)) {
				try {
					t.getOut().writeUTF("stop");
					t.interrupt();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static Connection privateChat(String ms, String user) {
		
		String split[] = ms.split(" ");
		System.out.println(split[0] + "  :  " + split[1]);
		
		for(Connection t : connectedServices) {
			if(split[1].equals(t.username) && !split[1].equals(user)) {
				try {
					t.getOut().writeUTF("private with " + user);
					return t;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//if user is not in the list broadkast it.
		Server.broadcast(ms, user);
		return null;
	}
}

class Connection extends Thread {
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;
	String username;
	Boolean admin = false;
	Boolean inPrivate = false;
	
	public Connection(Socket aClientSocket) {
		try {
		clientSocket = aClientSocket;
		in = new DataInputStream(clientSocket.getInputStream());
		out = new DataOutputStream(clientSocket.getOutputStream());
		username = in.readUTF();
		System.out.println(username + " has connected");
		for(String s : Server.adminList) {
			if(s.equals(username)) {
				admin = true;
			}
		}
		
		}catch(IOException e) {
			System.out.println("Connection: "+e.getMessage());
		}
	}

	public void run() {

		try {
			Server.connectedServices.add(this);
			while(!interrupted()) {
				String input = in.readUTF();
				
				//Client wants to disconnect
				if(input.equals("/stop")) {
					break;
				}
				
				//check if admin wants to kick someone
				if(admin && input.contains("/kick")) {
					Server.kickUser(input, username);
				}
				
				if(input.contains("/private") && !inPrivate) {
					Connection t = Server.privateChat(input, username);
					if(t != null) {
						PrivateChat o = new PrivateChat(this,t);
						Server.privateSessions.add(o);
						Server.connectedServices.remove(t);
						Server.connectedServices.remove(this);
						t.inPrivate = true;
						inPrivate = true;
						System.out.println(username + " and " + t.username + " has private chat.");
					}
				}
				
				if(inPrivate) {
					if(input.contains("/leave")) {
						System.out.println(username + "left private chat");
						for(PrivateChat p : Server.privateSessions) {
							if(p.getUser1().username.equals(username) || p.getUser2().username.equals(username)) {
								Server.privateBroadcast("leaving private chat", username);
								Server.connectedServices.add(p.getUser1());
								Server.connectedServices.add(p.getUser2());
								p.getUser1().inPrivate = false;
								p.getUser2().inPrivate = false;
								Server.privateSessions.remove(p);
								break;
							}
						}
					}else {
					Server.privateBroadcast(input, username);
					}
				}
				else {
					Server.broadcast(input, username);
				}
				
			}
			//when disconnecting
			Server.broadcast("Left the server", username);
			if(inPrivate) {
				for(PrivateChat p : Server.privateSessions) {
					if(p.getUser1().username.equals(username)) {
						Server.connectedServices.add(p.getUser2());
						p.getUser2().inPrivate = false;
						Server.privateSessions.remove(p);
						break;
					}
					else if(p.getUser2().username.equals(username)) {
						Server.connectedServices.add(p.getUser1());
						p.getUser1().inPrivate = false;
						Server.privateSessions.remove(p);
						break;
					}
				}
			}
			else {
				Server.connectedServices.remove(this);
			}
			System.out.println(username + " Left the server");
			clientSocket.close();
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO:" + e.getMessage());
		} finally {
		}

	}
	
	public DataOutputStream getOut() {
		return  out;
	}
}

class PrivateChat{
	static Connection user1;
	static Connection user2;
	public PrivateChat(Connection us1, Connection us2) {
		user1 = us1;
		user2 = us2;
	}
	
	public Connection getUser1() {
		return user1;
	}
	public Connection getUser2() {
		return user2;
	}
}
