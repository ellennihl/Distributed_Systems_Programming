package asgmt2;

import java.io.*;
import java.net.*;

public class Client extends Thread{
	
	Socket s;
	DataInputStream in;
	DataOutputStream out;
	BufferedReader msgReader;
	String userName;
	
	public Client() {
		try {
			s = new Socket("localhost", Server.PORT);
			in = new DataInputStream(s.getInputStream());
			out = new DataOutputStream(s.getOutputStream());
			msgReader = new BufferedReader(new InputStreamReader(System.in));//type message in console
		} catch (UnknownHostException e) {
			System.out.println("Sock:" + e.getMessage());
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO:" + e.getMessage());
		}
	}
	
	public void run() {
		while(!interrupted()) {
			try {
				if(in.available()>0)
					readMsg();
			} catch (IOException e) {
				e.printStackTrace();
				}
			}
		if(s != null) {
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}


	public void sendMsg() {
		System.out.print("Enter your username: ");
		String msg = "";
		try {
			userName = msgReader.readLine();
			out.writeUTF(userName);
			do {
				msg = msgReader.readLine();
				out.writeUTF(userName + "> "+ msg +" ("+new java.util.Date() + ")");
				//System.out.println(userName + ">");
			}
			while(!msg.equals("q"));//enter q for quit
			this.interrupt();
			//this.join();
			out.writeUTF(msg);
			System.out.println("KOMMER DU INTE HIT ELLER");
		}catch(IOException e) {
			System.out.println(e.getMessage());
		//} catch (InterruptedException e) {
			//e.printStackTrace();
		}
	}
	
	public void readMsg() {
			String reply = "";
			try {
				reply = in.readUTF();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(reply);
	}

	public static void main(String args[]) throws InterruptedException {
		/*Client client = new Client();
		receiveThread = new Thread(new Runnable() {
		      public void run(){
		    	  while(!Thread.interrupted())
		    		  client.readMsg();
		      }
		   });*/
		Client client = new Client();
		client.start();
		client.sendMsg();
	}
}
/*
class ReceiveThread extends Thread{
	Client client;
	
	public ReceiveThread(Client c) {
		client = c;
	}
	
	public void run() {
		while(!interrupted())
  		  client.readMsg();
    }
}*/
		/*Socket s = null;
		DataInputStream in = null;
		DataOutputStream out = null;*/
		//String userName = "";
		/*try {
			Client client = new Client();
			s = new Socket("localhost", Server.PORT);
			in = new DataInputStream(s.getInputStream());
			out = new DataOutputStream(s.getOutputStream());
			msgReader = new BufferedReader(new InputStreamReader(System.in));//type message in console
			System.out.println("Enter your username:");
			userName = msgReader.readLine();
			out.writeUTF(userName);
			String msg = "";// = msgReader.readLine();
			String reply = "";
			Thread receiveThread = new Thread(new Runnable() {
			      public void run(){
			    	  try {
						String reply = in.readUTF();
						System.out.println(reply);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			      }
			   });
			while(!msg.equals("q")) {//enter q for quit
				out.writeUTF("Message: "+ msg +" send at "+new java.util.Date());
				String reply = in.readUTF();			
				System.out.println("Received back: "+reply);
				msg = msgReader.readLine();
			}
			out.writeUTF(msg);
			
			
		} catch (UnknownHostException e) {
			System.out.println("Sock:" + e.getMessage());
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO:" + e.getMessage());
		} finally {
			if(s != null) {
				try {
					out.writeUTF(userName + " left the channel.");
					s.close();
				//in.close();
				//out.close();
				}catch(IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}*/
	//}

//}
