// Created by Eek de Bruijckere and Ellen Nihl 09-12-2021
package internet_chat_service;

import java.net.*;
import java.util.*;
import java.io.*;

/**
* A Client establishes a connection to a Server and spawns a thread that handles the receiving messages from the Server. 
* The user can create messages in the command line and send the messages to the Server which distributes the messages to
* all the other Clients.
*/
public class Client {
	
	static volatile boolean kicked = false;
	
	public static void main(String args[]) {
		Socket s = null;
		Scanner sc = new Scanner(System.in);
		String username = sc.nextLine();
		
		
		try {
			s = new Socket("localhost", Server.PORT);
			DataInputStream in = new DataInputStream(s.getInputStream());
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			
			//Say hello to server by sending the username
			out.writeUTF(username);
			
			//start a thread that catches sent data
			Thread catcher = new Catcher(in,out);//.start();
			catcher.start();
			
			while(true) {
				String send = sc.nextLine(); //vill egentligen kicka ut denna
				
				if(kicked) {
					catcher.join();
					break;
				}
				
				out.writeUTF(send);
				
				if(send.equals("/stop")) {
					catcher.interrupt();
					catcher.join();
					break;
				}
			}
		} catch (UnknownHostException e) {
			System.out.println("Sock:" + e.getMessage());
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO:" + e.getMessage());
		} catch (InterruptedException e) {
			System.out.println("Interrupt");
			e.printStackTrace();
		} finally {
			if(s != null) {
				try {
				s.close();
				sc.close();
				}catch(IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
}
/**
* Catcher handles the messages sent by the Server.
*/
class Catcher extends Thread{
	DataInputStream in;
	DataOutputStream out;
	public Catcher(DataInputStream input,DataOutputStream output) {
		in = input;
		out = output;
	}
	/**
	* The thread handles the messages sent by the server until the user types "/stop", then the thread is terminated.
	*/
	public void run() {
		try {
			while(!interrupted()) {
				String input = in.readUTF();
				if(input.equals("/stop")) {
					System.out.println("you have been kicked from the server");
					Client.kicked = true;
					out.writeUTF("/stop");
					break;
				}
				System.out.println(input);
			}
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO:" + e.getMessage());
		} finally {
		}

	}
}
