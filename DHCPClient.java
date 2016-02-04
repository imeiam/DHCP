import java.io.*;
import java.net.*;

public class DHCPClient{

	String clientIP=null;
	InetAddress serverIP;
	DatagramSocket socket;
	InetAddress broadAddr;
	String offeredIP;
	byte[] sendData;
	byte[] recvData;
	BufferedReader input=null;		
	boolean allocated = false; // whether IP address has be allocated or not!

	public void initializeClient(int port) throws Exception{
	
		socket = new DatagramSocket(port);
		String hostName= "255.255.255.255";
	        broadAddr = InetAddress.getByName(hostName);
	        sendData = new byte[1024];
	        recvData = new byte[1024];
		input = new BufferedReader(new InputStreamReader(System.in));
		
	}		
		
	public void startClient() throws Exception{
		
		sop("Initializing Client.. ");
		sleepThread();		
		sop("Client Initialization Complete! ");
		sleepThread();			
		
				
		DHCPMessage message = new DHCPMessage();
	        byte[] data  = convertMessToByteArr(message);
            	DatagramPacket sendPacket = new DatagramPacket(data,data.length,broadAddr, 2508);
	        socket.send(sendPacket);
                sop("\n\nSent DHCP-DISCOVER Request..");
                
                // Dicover Message sent
                
                DatagramPacket incomingPacket = new DatagramPacket(recvData, recvData.length);	
	        socket.receive(incomingPacket);
	        data = incomingPacket.getData();
	        ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
	        message = (DHCPMessage) is.readObject();
	        sleepThread();
	        serverIP = incomingPacket.getAddress();
	        offeredIP = message.getYIAddr();
	        sleepThread();
	        sleepThread();
	        sleepThread();
	        sop("Received DHCP-OFFER MESSAGE -  Response for DHCP DISCOVER  ");
	        sleepThread();
	        sop("Identified Server IP Address: "+serverIP.getHostAddress());
	                
                // Offer receveived.
                
                
                sop("\n\nIP Address Offered: "+offeredIP);
                boolean type=true;            
                boolean flag = true;
                do{
                	sop("\nAccept?(Y/n) ");
	                String str = input.readLine();
	                if(str.length()>1)
	                	continue;
	                if(str.toUpperCase().charAt(0)=='Y')
	                	flag = false;
	                else if(str.toLowerCase().charAt(0)=='n'){
	                	flag = false;
	                	type = false;	                
	                }
	                else 
	                	continue;	                		                
		}while(flag);	       
                
                // Offer accepted or declined depending upon the value of type. true - +ve false - -ve
                
                if(type){
                     sop("\nOffer Accepted!");
                     sleepThread(); 
                     sleepThread();
                     // message object sent by server is used by the Op is modified to denote request message
                     message.setOp(1);
	      	     data  = convertMessToByteArr(message);
	             sendPacket = new DatagramPacket(data,data.length,serverIP, 2508);
	 	     socket.send(sendPacket);                     
                     sop("Sent DHCP-REQUEST Message to the DHCP Server");
                }
                else{
		     sop("\nOffer Declined!");
                     sleepThread(); 
                     sleepThread();
                     // message object sent by server is used by the Op is modified to denote request message
                     message.setOp(2);
	      	     data  = convertMessToByteArr(message);
	             sendPacket = new DatagramPacket(data,data.length,serverIP, 2508);
	 	     socket.send(sendPacket);            
                     sop("Sent DHCP-DECLINE Message to the DHCP Server");                                   
                }
                
                // Sent ACCEPT OR DECLINE MESSAGE
                
                incomingPacket = new DatagramPacket(recvData, recvData.length);	
	        socket.receive(incomingPacket);
	        data = incomingPacket.getData();
	        in = new ByteArrayInputStream(data);
                is = new ObjectInputStream(in);
	        message = (DHCPMessage) is.readObject();
	        sleepThread();
	        sleepThread();	        
		
		sop("\nReceived DHCP-ACK message from the server! ");				
	        
	        // Receieved ACK from the DHCP server
	        
	        if(type){
	        	sleepThread();
	        	sleepThread();
	        	sop("\nIP Address: "+message.getCIAddr()+" has been allocated to this host using DHCP server successfully! ");
	        	
	        	sleepThread();
	        	sleepThread();
	        	sleepThread();
	        	boolean renewCheck=true;
	        	do{
		        	sop("\nLease Countdown starts");
		      		countdown(message.getCIAddr(),10);
		      		boolean test=true;
		      		int choice;  
		      		do{
	      				sop("\n\nLease time complete! \n\nOptions:\n1.DHCP-RENEW\n2.DHCP-RELEASE\n\nEnter your choice: ");		        	
			        	switch((choice =Integer.parseInt(input.readLine()))){
			        		case 1:	
			        			sleepThread();
			        			message.setOp(3);
    				   		      	data  = convertMessToByteArr(message);
		         			        sendPacket = new DatagramPacket(data,data.length,serverIP, 2508);
		 	     				socket.send(sendPacket);                     
        	             				sop("Sent DHCP-RENEW Message to the DHCP Server");
			        			test=false;
							break;
						case 2:
							sleepThread();
							message.setOp(4);
    				   		      	data  = convertMessToByteArr(message);
		         			        sendPacket = new DatagramPacket(data,data.length,serverIP, 2508);
		 	     				socket.send(sendPacket);                     
        	             				sop("Sent DHCP-RELEASE Message to the DHCP Server");
							test=false;
							renewCheck=false;
							break;		        		
			        	}
			        }while(test);
			        incomingPacket = new DatagramPacket(recvData, recvData.length);	
			        socket.receive(incomingPacket);
			        data = incomingPacket.getData();
			        in = new ByteArrayInputStream(data);
        		        is = new ObjectInputStream(in);
			        message = (DHCPMessage) is.readObject();
			        sleepThread();
			        sleepThread();			       
				sop("\nReceived DHCP-ACK message from the server! ");				
	        
			        // Receieved ACK from the DHCP server
	        
			}while(renewCheck);        
			
			sleepThread();
			sleepThread();
			shutdown(5);
			sop("\n\n* Client Shutdown * ");
			System.exit(0);

	        }
	        else{
	        	sleepThread();
	        	sleepThread();
	        	sop("\nClient will reboot and start the process again!");
	        	sleepThread();
	        	sleepThread();	        	
	        }
	        	        	                                        			
	}
	
	
	public void countdown(String IP,int i) throws Exception{
		for(int j=i;j>0;j--){
			Thread.sleep(1000);
			sop("\nLease For IP: "+IP+" ends in "+j+" seconds! ");
		}
	}
	
	public void setServerIP(String ip) { this.clientIP = ip; }

	public String getClientIP() { return this.clientIP; }
	
	
	public byte[] convertMessToByteArr(DHCPMessage message) throws Exception{
	
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            ObjectOutputStream os = new ObjectOutputStream(outputStream);
	
            os.writeObject(message);
	    byte[] data = outputStream.toByteArray();
            return(data);
	}
	
	
	
	public void shutdown(int i) throws Exception{
		for(int j=i;j>0;j--){
			Thread.sleep(1000);
			sop("\nClient Shutting down in "+j+" seconds! ");
		}
	}
	
	
		
	
	public static void sleepThread() throws Exception { Thread.sleep(1000); }
	
	
	public static void sop(String x){
		System.out.println(x);
	}

	public static void main(String [] args){
		try{
			sop("\n* Client Bootup *\n");
			sleepThread();
			DHCPClient client = new DHCPClient();
			client.initializeClient(Integer.parseInt(args[0]));
			do{
				client.startClient();
			}while(!client.allocated);
				
		}
		catch(Exception e){	e.printStackTrace(); }
	}
}

