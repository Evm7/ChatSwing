package ChatSwing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class ChatClient {

    private Set<String> users = new HashSet<>();
    private static String host;
    private static int portNumber = 5000;
    private String username;
    private String state;

    //Swing Objects
    JFrame frame = new JFrame("Chatter");
    JTextField textField = new JTextField(75);
    JList messageArea, userArea;
    DefaultListModel messages, usernames;

    public ChatClient() {
        //First state is ACCEPT, which asks for username
        this.state = "ACCEPT";

        //Create Swing Jframe and containers
        //Area of messages
        this.messages = new DefaultListModel();
        this.messageArea = new JList(messages);
        this.messageArea.setBackground(new Color(180, 233, 184));
        messageArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        //Area of Users Online
        TitledBorder titleUsers = new TitledBorder("Users Online");
        titleUsers.setTitleColor(new Color(91, 120, 25));
        this.usernames = new DefaultListModel();
        this.userArea = new JList(usernames);
        this.userArea.setBackground(new Color(223, 233, 180));
        this.userArea.setBorder(titleUsers);

        //Pack all Jobjects into frmae and organized the visualitzation
        textField.setEditable(false);
        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(userArea), BorderLayout.WEST);
        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.pack();

    }

    private void startClient() {
        try {
            //Create Thread to managed in/outcoming messages from server
            ClientThread clientThread = new ClientThread(this);
            Thread serverAccessThread = new Thread(clientThread);
            serverAccessThread.start();
            
            // Bind enter event as message and output through ClientThread to server
            textField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    clientThread.addNextMessage(textField.getText());
                    textField.setText("");
                }
            });
            while (serverAccessThread.isAlive()) {
                switch (state) {
                    //If ACCEPT State: ask user for username and output to Server
                    case "ACCEPT":
                        clientThread.addNextMessage(this.getName());
                        this.state = "JOIN";
                        break;
                    //Exit 
                    case "QUIT":
                        return;
                    default:
                        break;
                }
            }

        } catch (Exception ex) {
            System.out.println("Error while starting client: " + ex);
        } finally {
            //If client stops, close Swing Frame
            frame.setVisible(false);
            frame.dispose();
        }
    }

    //Update users connected to the chat
    public void updateUsers(String users) {
        users = users.substring(1, users.length() - 1);
        String[] update;
        if (!users.contains(",")) {
            this.users.add(users);
        } else {
            this.users = new HashSet<String>(Arrays.asList(users.split(", ")));
        }
        this.visualizeUsers();
    }

    //Update Users Area
    public void visualizeUsers() {
        usernames.removeAllElements();
        Iterator iterador = users.iterator();
        while (iterador.hasNext()) {
            String next = (String) iterador.next();
            usernames.addElement(next);
        }
    }

    //Asked for username
    public String getName() {
        return JOptionPane.showInputDialog(
                frame,
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setName(String username) {
        this.username = username;
    }

    public String getHost() {
        return this.host;
    }

    public int getPortNumber() {
        return this.portNumber;
    }

    public String getState() {
        return this.state;
    }

    public static void main(String[] args) {
        host = "localhost";
        portNumber = 5000;
        if (args.length <= 2 && args.length > 0) {
            if (!args[0].isEmpty()) {
                host = args[0];
                if (!args[1].isEmpty()) {
                    portNumber = Integer.parseInt(args[1]);
                }
            }
        }

        ChatClient client = new ChatClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.startClient();
    }
}
