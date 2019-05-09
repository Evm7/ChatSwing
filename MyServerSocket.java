package ChatSwing;

import java.io.IOException;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;


public class MyServerSocket extends ServerSocket {

	public MyServerSocket() throws IOException  {
		super();

	}
	
	public MyServerSocket(int port_number) throws IOException {

	           super(port_number);
	}
	
	public MyServerSocket(int port, int backlog) throws IOException {	
	           super(port, backlog);
	}
	
	public MyServerSocket(int port, int backlog, InetAddress bindAddr) throws IOException{
		
	           super(port, backlog, bindAddr);	  

	}
	
	
	public void bind(SocketAddress endpoint){
		try {
	           super.bind(endpoint);;
	        		  
	    }
	    catch (IOException e) {
	        System.out.println("Failed to bind at "+endpoint);
	    }
	}
	
	public void bind(SocketAddress endpoint, int backlog){
		try {
	           super.bind(endpoint, backlog);
	        		  
	    }
	    catch (IOException e) {
	        System.out.println("Failed to bind at "+endpoint+" and backlog "+backlog);
	    }
	}
	
	
	public Socket accept() {
		Socket s = null;
		try {
	           s = super.accept();
	        		  
	    }
	    catch (IOException e) {
	        System.out.println("Accept failed on the port "+super.getLocalPort());
	    }
		return s;
	}
	
	public MySocket my_accept() {
		MySocket mysocket = null;
		try {
		       Socket socket = super.accept();
		       mysocket = new MySocket(socket);
		        }
		    catch (IOException e) {
		       System.out.println("Accept failed on the port "+super.getLocalPort());
		    }
		
		return mysocket;
	}
	
	public void close() {
		try {
		       super.close();
		        }
		    catch (IOException e) {
		       System.out.println("Error closing the server: \n"+e);
		    }
		
	}
	
	public void setSoTimeout(int timeout) {
		try {
		       super.setSoTimeout(timeout);
		        }
		    catch (IOException e) {
		       System.out.println("Error setting the timeout: \n"+e);
		    }
	}
	
	public int getSoTimeout() {
		try {
		       return super.getSoTimeout();
		        }
		    catch (IOException e) {
		       System.out.println("Error getting the timeout: \n"+e);
		    }
		return 0;
	}
	
	public void setReuseAddress(boolean on) {
		try {
		       super.setReuseAddress(on);
		        }
		    catch (IOException e) {
		       System.out.println("Error setting the reused address: \n"+e);
		    }
	}
	
	public boolean getReuseAddress() {
		try {
		       return super.getReuseAddress();
		        }
		    catch (IOException e) {
		       System.out.println("Error getting the reused address: \n"+e);
		    }
		return false;
	}
	
	
	public void setReceiveBufferSize(int size) {
		try {
		       super.setReceiveBufferSize(size);
		        }
		    catch (IOException e) {
		       System.out.println("Error setting the buffer size: \n"+e);
		    }
	}
	
	public int getReceiveBufferSize() {
		int buff_size = 0;
		try {
		       buff_size = super.getReceiveBufferSize();
		        }
		    catch (IOException e) {
		       System.out.println("Error getting the buffer size: \n"+e);
		    }
		return buff_size;
	}
	
	


}
