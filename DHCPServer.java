import java.io.*;
import java.net.*;

public class DHCPServer implements Runnable{

	String serverIP=null;

	DatagramSocket serverSock=null;
	
	byte[] send_data;
	
	byte[] recv_data;
	
	InetAddress clientAddr;
	int clientPort;
	InetAddress broadAddr;
	


	public static String getLocalIP() throws Exception
	{
		int i, j;
		String localIP = "";
		String cmd = "bash sh.sh"; // execute ifconfig
		Process p = Runtime.getRuntime().exec(cmd);
 		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String in = stdInput.readLine(); 	// get output of command execution
		for(i = 0; i < in.length(); i++)       // find end of IPAddress
			if(in.charAt(i) == ' ')
				break;
		for(j = 0; j < i; j++)  		// extract IPAddress
			localIP = localIP + in.charAt(j);
		return localIP;
	}

	public void initializeServer() throws Exception{
		serverIP = getLocalIP();
		serverSock=new DatagramSocket(2508);
		send_data = new byte[1024];
		recv_data = new byte[1024];
		String hostName= "255.255.255.255";
	        broadAddr = InetAddress.getByName(hostName);
		IPManager.initializeMap();
	}

	public void receiveMessage() throws Exception{
		
		sop("Server Waiting on Broadcast Port: 2508 ");
		DHCPMessage message;
		
		while(true){
			DatagramPacket incomingPacket = new DatagramPacket(recv_data, recv_data.length);	
	                serverSock.receive(incomingPacket);
	                byte[] data = incomingPacket.getData();
	                ByteArrayInputStream in = new ByteArrayInputStream(data);
                        ObjectInputStream is = new ObjectInputStream(in);
	                message = (DHCPMessage) is.readObject();
	                
	                clientAddr = incomingPacket.getAddress();
	                clientPort = incomingPacket.getPort();
	                switch(message.getOp()){
	                	case 0:	      
	                		sleepThread();      
	                		sleepThread();
	                		sleepThread();
	                		sop("\n\nReceieved DHCP DISCOVER Request from Client Port: "+clientPort);
	                		sleepThread();
	                		afterDiscover();
	                		sleepThread();
	                		sop("Sent DHCP OFFER -  Response for DHCP DISCOVER.. ");
	                		break;
	                	case 1: 
	                		sleepThread();      
	                		sleepThread();
	                		sleepThread();
	                		sop("\n\nReceieved DHCP REQUEST from Client Port: "+clientPort);
	                		sleepThread();
	                		sleepThread();
	                		message.setCIAddr(message.getYIAddr());
	                		sop("Allocated IP Address: "+message.getCIAddr()+" to the Client! ");
	                		sendAck(clientAddr,message);
	                		break;
	                	case 2:
	                		sleepThread();      
	                		sleepThread();
	                		sleepThread();
	                		sop("\n\nReceieved DHCP DECLINE request from Client Port: "+clientPort);
	                		sleepThread();
	                		sleepThread();
	                		IPManager.freeEntry(message.getYIAddr());
	                		sop("\nIP manager Freed IP Address: "+message.getYIAddr()+" from the Client! ");
	                		sendAck(clientAddr,message);
	                		break;
	                	case 3:
	                		sleepThread();      
	                		sleepThread();
	                		sleepThread();
	                		sop("\n\nReceieved DHCP RENEW request from Client Address: "+message.getCIAddr()+" Port: "+clientPort);
	                		sleepThread();
	                		sleepThread();
	                		message.setCIAddr(message.getYIAddr());
	                		sop("Renewed IP Address lease: "+message.getCIAddr()+" for Client! ");
	                		sendAck(clientAddr,message);
	                		break;	                	
				case 4:
	                		sleepThread();      
	                		sleepThread();
	                		sleepThread();
	                		sop("\n\nReceieved DHCP RELEASE request from Client Address: "+message.getCIAddr()+" Port: "+clientPort);
	                		sleepThread();
	                		sleepThread();
	                		IPManager.freeEntry(message.getYIAddr());
	                		sop("Released IP Address: "+message.getCIAddr()+" from the Client! ");
	                		sendAck(clientAddr,message);
	                		break;	                		                			
	                		
	                }
		}
				
		
	}
	
	public void sendAck(InetAddress clientAddr,DHCPMessage message) throws Exception{		
		
		message.setOp(7);		
	        byte[] data  = convertMessToByteArr(message);
            	DatagramPacket sendPacket = new DatagramPacket(data,data.length,clientAddr, clientPort);
	        serverSock.send(sendPacket);
	        sleepThread();
	        sleepThread();
	        sop("\nSent DHCP ACK ");
	}
	

	public void afterDiscover() throws Exception{
		
		
		DHCPMessage message = new DHCPMessage();
		message.setOp(9);
		String str = IPManager.getRandomFreeIP();
		message.setYIAddr(str);
		message.setSIAddr(serverIP);
		
	        byte[] data  = convertMessToByteArr(message);
            	DatagramPacket sendPacket = new DatagramPacket(data,data.length,broadAddr, clientPort);
	        serverSock.send(sendPacket);
	}

	public byte[] convertMessToByteArr(DHCPMessage message) throws Exception{
	
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            ObjectOutputStream os = new ObjectOutputStream(outputStream);
	
            os.writeObject(message);
	    byte[] data = outputStream.toByteArray();
            return(data);
	}

	@Override
	public void run(){
	}

	public void setServerIP(String ip) { this.serverIP = ip; }

	public String getServerIP() { return this.serverIP; }

	public static void main(String [] args){
		try{
			DHCPServer server = new DHCPServer();
			sop("\n* Server Bootup *\n");
			sleepThread();
			sleepThread();			
			sop("Initializing Server.. ");
			sleepThread();
			sleepThread();			
			server.initializeServer();	
			sop("Server Initialization Complete! ");
			sleepThread();
			sleepThread();						
			sop("Configuring Server to listen on BroadCast Port: 2508 ");
			sleepThread();
			sleepThread();				
			sop("Configuration Complete! ");		
			server.receiveMessage();
		}
		catch(Exception e) {}
	}
	
	public static void sleepThread() throws Exception { Thread.sleep(1000); }
	
	
	public static void sop(String x){
		System.out.println(x);
	}
}

