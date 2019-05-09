package ChatSwing;



import java.util.LinkedList;

public class ClientThread implements Runnable {

    private MySocket mysocket;
    private String user_name;
    private ChatClient c;
    private final LinkedList<String> messagesToSend;
    private boolean hasMessages = false;

    public ClientThread(ChatClient c) {
        this.c = c;
        this.mysocket = new MySocket(c.getHost(), c.getPortNumber());
        this.messagesToSend = new LinkedList<String>();
    }

    //Add new message introduced by user toMessage buffer
    public void addNextMessage(String message) {
        synchronized (messagesToSend) {
            this.hasMessages = true;
            this.messagesToSend.push(message);
        }
    }

    @Override
    public void run() {
        System.out.println("Local Port : " + mysocket.getLocalPort());
        System.out.println("Servidor = " + mysocket.getRemoteSocketAddress() + ":" + mysocket.getPort());

        mysocket.flush();
        while (!mysocket.isClosed()) {
            if (mysocket.ready()) {
                String line = this.mysocket.readLine();
                if (line != null) {
                    //Parsing messages
                    String[] message = line.split(":", 2);
                    switch (message[0]) {
                        //Server asking for username
                        case "ACCEPT":
                            c.setState("JOIN");
                            break;
                        //Server confirming name
                        case "JOIN":
                            this.user_name = message[1];
                            c.setName(message[1]);
                            c.frame.setTitle("Chat - " + this.user_name);
                            c.textField.setEditable(true);
                            c.setState("MESSAGE");
                            break;
                        //Server closing client
                        case "QUIT":
                            mysocket.println("QUIT:" + user_name);
                            System.out.println("GoodBye");
                            c.setState("QUIT");
                            return;
                        //Incoming message from server
                        case "MESSAGE":
                            c.messages.addElement(message[1]+'\n');
                            break;
                        //Update users connected (user has joined or drop)
                        case "USERS":
                            c.updateUsers(message[1]);
                            break;
                    }
                }
            }
            //Manage user's message buffer. Connection to ChatClient
            if (hasMessages) {
                String nextSend = "";
                synchronized (messagesToSend) {
                    nextSend = messagesToSend.pop();
                    if (nextSend.startsWith("QUIT:")) {
                        c.setState("QUIT");
                        break;
                    }
                    hasMessages = !messagesToSend.isEmpty();
                }

                if (c.getState() == "ACCEPT" || c.getState() == "JOIN") {
                    mysocket.println(nextSend);
                } else {
                    mysocket.println(this.user_name + " -> " + nextSend);
                }
                mysocket.flush();

            }
        }
    }
}
