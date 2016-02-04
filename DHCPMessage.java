import java.io.*;
public class DHCPMessage implements Serializable{

	protected int Op=0;  //  1 - request 2 - response

	protected String CIAddr="000.000.000.000";  // Used to store the Client IP Address. 0 if not Assigned.

	protected String YIAddr="000.000.000.000"; // IP Address provided by DHCP Server

	protected String SIAddr="000.000.000.000"; // Server IP address

	public void setOp(int Op) { this.Op = Op; }

	public void setCIAddr(String x) { this.CIAddr = x; }

	public void setYIAddr(String x) { this.YIAddr = x; }

	public void setSIAddr(String x) { this.SIAddr = x; }
	
	public int getOp() { return this.Op; }

	public String getCIAddr() { return this.CIAddr; }

	public String getYIAddr() { return this.YIAddr; }

	public String getSIAddr() { return this.SIAddr; }
	
	public String toString(){ return(""+Op+" "+CIAddr+" "+YIAddr+" "+SIAddr+" "); }
}

/*


 	0 - discover   
  	1 - request
	2 - decline
	3 - renew
	4 - release
	
	
	
	7-ack
	8 - nack
	9- offer

*/
