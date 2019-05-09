package ChatSwing;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySocket {

    Socket mysocket;
    String username;
    BufferedReader input;
    PrintWriter output;

    public MySocket(String host, int port) {
        try {
            this.mysocket = new Socket(host, port);
            this.input = new BufferedReader(new InputStreamReader(this.mysocket.getInputStream()));
            this.output = new PrintWriter(this.mysocket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public MySocket(String username, String host, int port) {
        try {
            this.username = username;
            this.mysocket = new Socket(host, port);
            this.input = new BufferedReader(new InputStreamReader(this.mysocket.getInputStream()));
            this.output = new PrintWriter(this.mysocket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public MySocket(Socket s) {
        try {
            this.mysocket = s;
            this.input = new BufferedReader(new InputStreamReader(this.mysocket.getInputStream()));
            this.output = new PrintWriter(this.mysocket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public Socket getSocket() {
        return this.mysocket;
    }

    public String getName() {
        return this.username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public BufferedReader getInputStream() {
        return this.input;
    }

    public PrintWriter getOutputStream() {
        return this.output;
    }

    public int getLocalPort() {
        return this.mysocket.getLocalPort();
    }

    public SocketAddress getRemoteSocketAddress() {
        return this.mysocket.getRemoteSocketAddress();
    }

    public void flush() {
        this.output.flush();
    }

    public int getPort() {
        return this.mysocket.getPort();
    }

    public void close() {
        try {
            this.output.close();
            this.input.close();
            this.mysocket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public boolean isClosed() {
        return this.mysocket.isClosed();
    }

    public boolean ready() {
        try {
            return this.input.ready();
        } catch (IOException ex) {
            System.out.println("Not ready to read: " + ex);
        }
        return false;
    }

    public String readLine() {
        String s = null;
        try {
            s = this.input.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }

    public void writeLine(String s) {
        this.output.write(s);
    }

    public void println(String s) {
        this.output.println(s);
    }

    public void sendUsername() {
        //this.writeLine(this.username);
        this.output.println(this.username);
        System.out.println("USERNAME SENT: " + this.username);
    }

    public String receiveUsername() {
        return this.readLine();
    }

    public String setUsername() {
        this.username = this.readLine();
        return this.username;
    }
}
